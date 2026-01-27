package ao.kuilu.controller;

import ao.kuilu.dto.FilaRequest;
import ao.kuilu.dto.FilaResponse;
import ao.kuilu.dto.PosicaoFilaResponse;
import ao.kuilu.repository.FilaRepository;
import ao.kuilu.service.FilaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/filas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Filas", description = "API de gestão de filas")
public class FilaController {

    private final FilaService filaService;
    private final FilaRepository filaRepository;

    /**
     * POST /filas
     * Criar nova fila (ADMIN only)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar nova fila", description = "Apenas administradores podem criar filas")
    public Mono<ResponseEntity<FilaResponse>> criarFila(@Valid @RequestBody FilaRequest request) {
        log.info("Criando nova fila: {}", request.getNome());

        return filaService.criarFila(request.getNome(), request.getLocal(), request.getTempoMedioAtendimento())
                .map(fila -> FilaResponse.builder()
                        .id(fila.getId())
                        .nome(fila.getNome())
                        .local(fila.getLocal())
                        .ativa(fila.getAtiva())
                        .tempoMedioAtendimento(fila.getTempoMedioAtendimento())
                        .criadaEm(fila.getCriadaEm())
                        .build())
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    /**
     * GET /filas/{id}
     * Obter detalhes da fila
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes da fila", description = "Retorna informações da fila")
    public Mono<ResponseEntity<FilaResponse>> obterFila(@PathVariable UUID id) {
        return filaRepository.findById(id)
                .map(fila -> FilaResponse.builder()
                        .id(fila.getId())
                        .nome(fila.getNome())
                        .local(fila.getLocal())
                        .ativa(fila.getAtiva())
                        .tempoMedioAtendimento(fila.getTempoMedioAtendimento())
                        .criadaEm(fila.getCriadaEm())
                        .build())
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    /**
     * POST /filas/{id}/entrar
     * Entrar na fila (CLIENTE)
     */
    @PostMapping("/{id}/entrar")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Entrar na fila", description = "Cliente entra na fila")
    public Mono<ResponseEntity<?>> entrarNaFila(
            @PathVariable UUID id,
            @RequestParam UUID usuarioId) {

        log.info("Usuário {} entrando na fila {}", usuarioId, id);

        return filaService.entrarNaFila(id, usuarioId)
                .map(entrada -> ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                        "mensagem", "Você entrou na fila",
                        "numero", entrada.getNumeroSequencia(),
                        "filaId", entrada.getFilaId()
                )))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "erro", "Erro ao entrar na fila"
                )));
    }

    /**
     * POST /filas/{id}/chamar-proximo
     * Chamar próximo da fila (ADMIN only)
     */
    @PostMapping("/{id}/chamar-proximo")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Chamar próximo", description = "Administrador chama próximo da fila")
    public Mono<ResponseEntity<?>> chamarProximo(@PathVariable UUID id) {
        log.info("Chamando próximo da fila {}", id);

        return filaService.chamarProximo(id)
                .map(entrada -> ResponseEntity.ok(Map.of(
                        "mensagem", "Próximo chamado",
                        "usuarioId", entrada.getUsuarioId(),
                        "numero", entrada.getNumeroSequencia()
                )))
                .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "erro", "Nenhum usuário na fila"
                )));
    }

    /**
     * GET /filas/{id}/posicao/{usuarioId}
     * Obter posição do usuário na fila (CLIENTE)
     */
    @GetMapping("/{id}/posicao/{usuarioId}")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Obter posição na fila", description = "Cliente visualiza sua posição")
    public Mono<ResponseEntity<PosicaoFilaResponse>> obterPosicao(
            @PathVariable UUID id,
            @PathVariable UUID usuarioId) {

        log.info("Obtendo posição do usuário {} na fila {}", usuarioId, id);

        return filaService.obterPosicaoUsuario(id, usuarioId)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}

import java.util.Map;
