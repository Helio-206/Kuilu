package ao.kuilu.repository;

import ao.kuilu.domain.model.Fila;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FilaRepository extends ReactiveCrudRepository<Fila, UUID> {
}
