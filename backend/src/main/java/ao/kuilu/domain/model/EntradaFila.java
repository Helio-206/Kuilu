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
@Table("entradas_fila")
public class EntradaFila {

    @Id
    private UUID id;

    @Column("fila_id")
    private UUID filaId;

    @Column("usuario_id")
    private UUID usuarioId;

    @Column("numero_sequencia")
    private Integer numeroSequencia;

    @Column("entrou_em")
    private LocalDateTime entrouEm;

    @Column("saiu_em")
    private LocalDateTime saiuEm;
}
