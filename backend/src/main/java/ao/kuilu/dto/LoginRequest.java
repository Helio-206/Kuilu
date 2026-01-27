package ao.kuilu.dto;

import ao.kuilu.domain.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotNull(message = "usuarioId é obrigatório")
    private UUID usuarioId;

    @NotNull(message = "role é obrigatória")
    private Role role;
}
