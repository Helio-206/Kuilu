package ao.kuilu.domain.model;

import ao.kuilu.domain.enums.Role;
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
@Table("usuarios")
public class Usuario {

    @Id
    private UUID id;

    @Column("nome")
    private String nome;

    @Column("telefone")
    private String telefone;

    @Column("role")
    private Role role;

    @Column("criado_em")
    private LocalDateTime criadoEm;
}
