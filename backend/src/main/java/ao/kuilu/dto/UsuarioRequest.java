package ao.kuilu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome do usuário", example = "Nataniel Hélio")
    private String nome;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Telefone inválido")
    @Schema(description = "Telefone do usuário", example = "+244923456789")
    private String telefone;
}
