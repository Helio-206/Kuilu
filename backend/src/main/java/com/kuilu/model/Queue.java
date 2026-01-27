package com.kuilu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("queues")
public class Queue {

    @Id
    private UUID id;

    @Column("service_id")
    private UUID serviceId;

    @Column("date")
    private LocalDate date;

    @Column("current_number")
    private Integer currentNumber;

    @Column("status")
    private QueueStatus status;
}
