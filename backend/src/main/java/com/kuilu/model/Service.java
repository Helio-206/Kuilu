package com.kuilu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("services")
public class Service {

    @Id
    private UUID id;

    @Column("institution_id")
    private UUID institutionId;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("avg_service_time_minutes")
    private Integer avgServiceTimeMinutes;

    @Column("active")
    private Boolean active;
}
