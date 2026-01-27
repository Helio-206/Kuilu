package com.kuilu.model;

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
@Table("institutions")
public class Institution {

    @Id
    private UUID id;

    @Column("name")
    private String name;

    @Column("address")
    private String address;

    @Column("phone")
    private String phone;

    @Column("created_at")
    private LocalDateTime createdAt;
}
