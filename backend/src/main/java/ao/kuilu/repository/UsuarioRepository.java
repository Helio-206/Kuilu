package ao.kuilu.repository;

import ao.kuilu.domain.model.Usuario;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsuarioRepository extends ReactiveCrudRepository<Usuario, UUID> {
}
