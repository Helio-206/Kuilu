package ao.kuilu.service;

import ao.kuilu.domain.model.EntradaFila;
import ao.kuilu.domain.model.Fila;
import ao.kuilu.dto.PosicaoFilaResponse;
import ao.kuilu.repository.EntradaFilaRepository;
import ao.kuilu.repository.FilaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilaService {

    private final FilaRepository filaRepository;
    private final EntradaFilaRepository entradaFilaRepository;

    /**
     * Cria uma nova fila
     */
    public Mono<Fila> criarFila(String nome, String local, Integer tempoMedioAtendimento) {
        Fila fila = Fila.builder()
                .id(UUID.randomUUID())
                .nome(nome)
                .local(local)
                .ativa(true)
                .tempoMedioAtendimento(tempoMedioAtendimento)
                .criadaEm(LocalDateTime.now())
                .build();

        return filaRepository.save(fila)
                .doOnSuccess(f -> log.info("Fila criada: {} - {}", f.getId(), f.getNome()))
                .onErrorMap(e -> new RuntimeException("Erro ao criar fila: " + e.getMessage()));
    }

    /**
     * Entra na fila
     * Garante que usuário não entra duplicado
     * Calcula número sequencial atomicamente
     */
    public Mono<EntradaFila> entrarNaFila(UUID filaId, UUID usuarioId) {
        return filaRepository.findById(filaId)
                .switchIfEmpty(Mono.error(new RuntimeException("Fila não encontrada")))
                .flatMap(fila -> {
                    if (!fila.getAtiva()) {
                        return Mono.error(new RuntimeException("Fila inativa"));
                    }

                    // Verificar se usuário já está na fila
                    return entradaFilaRepository.findActiveByFilaIdAndUsuarioId(filaId, usuarioId)
                            .flatMap(existingEntry -> {
                                log.info("Usuário {} já está na fila {}", usuarioId, filaId);
                                return Mono.just(existingEntry);
                            })
                            .switchIfEmpty(criarEntradaFila(filaId, usuarioId));
                })
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                .onErrorMap(e -> new RuntimeException("Erro ao entrar na fila: " + e.getMessage()));
    }

    /**
     * Cria entrada na fila com número sequencial
     */
    private Mono<EntradaFila> criarEntradaFila(UUID filaId, UUID usuarioId) {
        // Obter próximo número sequencial
        return entradaFilaRepository.findActiveByFilaId(filaId)
                .map(e -> e.getNumeroSequencia())
                .defaultIfEmpty(0)
                .reduce(0, Math::max)
                .map(maxNumero -> maxNumero + 1)
                .flatMap(proximoNumero -> {
                    EntradaFila entrada = EntradaFila.builder()
                            .id(UUID.randomUUID())
                            .filaId(filaId)
                            .usuarioId(usuarioId)
                            .numeroSequencia(proximoNumero)
                            .entrouEm(LocalDateTime.now())
                            .build();

                    return entradaFilaRepository.save(entrada)
                            .doOnSuccess(e -> log.info("Usuário {} entrou na fila {} com número {}", 
                                    usuarioId, filaId, proximoNumero));
                });
    }

    /**
     * Chama próximo da fila
     * Marca entrada como saída
     */
    public Mono<EntradaFila> chamarProximo(UUID filaId) {
        return entradaFilaRepository.findActiveByFilaId(filaId)
                .next()
                .flatMap(entrada -> {
                    entrada.setSaiuEm(LocalDateTime.now());
                    return entradaFilaRepository.save(entrada)
                            .doOnSuccess(e -> log.info("Próximo chamado da fila {}: número {}", 
                                    filaId, e.getNumeroSequencia()));
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Nenhum usuário na fila")));
    }

    /**
     * Obtém posição do usuário na fila
     */
    public Mono<PosicaoFilaResponse> obterPosicaoUsuario(UUID filaId, UUID usuarioId) {
        return entradaFilaRepository.findActiveByFilaIdAndUsuarioId(filaId, usuarioId)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuário não encontrado na fila")))
                .flatMap(entrada -> 
                    entradaFilaRepository.countWaitingBefore(filaId, entrada.getNumeroSequencia())
                            .flatMap(posicao -> 
                                filaRepository.findById(filaId)
                                        .map(fila -> PosicaoFilaResponse.builder()
                                                .usuarioId(usuarioId)
                                                .filaId(filaId)
                                                .posicao(posicao.intValue() + 1)
                                                .tempoEstimadoMinutos((int) ((posicao + 1) * fila.getTempoMedioAtendimento()))
                                                .ativo(true)
                                                .build()
                                        )
                            )
                );
    }
}
