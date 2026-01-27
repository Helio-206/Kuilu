package ao.kuilu.repository;

import ao.kuilu.domain.model.EntradaFila;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface EntradaFilaRepository extends ReactiveCrudRepository<EntradaFila, UUID> {

    @Query("SELECT * FROM entradas_fila WHERE fila_id = :filaId AND usuario_id = :usuarioId AND saiu_em IS NULL LIMIT 1")
    Mono<EntradaFila> findActiveByFilaIdAndUsuarioId(UUID filaId, UUID usuarioId);

    @Query("SELECT * FROM entradas_fila WHERE fila_id = :filaId AND saiu_em IS NULL ORDER BY numero_sequencia ASC")
    Flux<EntradaFila> findActiveByFilaId(UUID filaId);

    @Query("SELECT COUNT(*) FROM entradas_fila WHERE fila_id = :filaId AND saiu_em IS NULL AND numero_sequencia < :numeroSequencia")
    Mono<Long> countWaitingBefore(UUID filaId, Integer numeroSequencia);
}
