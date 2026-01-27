package ao.kuilu.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("filas")
public class Fila {

    @Id
    private UUID id;

    @Column("nome")
    private String nome;

    @Column("local")
    private String local;

    @Column("ativa")
    private Boolean ativa;

    @Column("tempo_medio_atendimento")
    private Integer tempoMedioAtendimento;

    @Column("criada_em")
    private LocalDateTime criadaEm;
}
