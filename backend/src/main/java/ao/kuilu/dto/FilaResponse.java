package ao.kuilu.dto;

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
public class FilaResponse {

    private UUID id;
    private String nome;
    private String local;
    private Boolean ativa;
    private Integer tempoMedioAtendimento;
    private LocalDateTime criadaEm;
    private Integer totalEsperando;
}
