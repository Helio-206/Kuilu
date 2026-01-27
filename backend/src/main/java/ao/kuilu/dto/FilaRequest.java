package ao.kuilu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilaRequest {

    @NotBlank(message = "Nome da fila é obrigatório")
    @Schema(description = "Nome da fila", example = "Atendimento ao Cliente")
    private String nome;

    @NotBlank(message = "Local da fila é obrigatório")
    @Schema(description = "Local onde fila funciona", example = "Balcão Principal")
    private String local;

    @Positive(message = "Tempo médio deve ser maior que zero")
    @Schema(description = "Tempo médio de atendimento em minutos", example = "15")
    private Integer tempoMedioAtendimento;
}
