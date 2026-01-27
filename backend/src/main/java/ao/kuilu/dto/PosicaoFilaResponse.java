package ao.kuilu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PosicaoFilaResponse {

    private UUID usuarioId;
    private UUID filaId;
    private Integer posicao;
    private Integer tempoEstimadoMinutos;
    private Boolean ativo;
}
