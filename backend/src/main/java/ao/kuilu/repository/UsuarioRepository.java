package ao.kuilu.repository;

import ao.kuilu.domain.model.Usuario;
import org.springframework.data.r2dbc.repository.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsuarioRepository extends ReactiveCrudRepository<Usuario, UUID> {
}
