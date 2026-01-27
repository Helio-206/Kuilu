package ao.kuilu.dto;

import ao.kuilu.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {

    private UUID id;
    private String nome;
    private String telefone;
    private Role role;
    private LocalDateTime criadoEm;
}
