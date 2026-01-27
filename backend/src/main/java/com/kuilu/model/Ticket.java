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
@Table("tickets")
public class Ticket {

    @Id
    private UUID id;

    @Column("queue_id")
    private UUID queueId;

    @Column("user_id")
    private UUID userId;

    @Column("number")
    private Integer number;

    @Column("status")
    private TicketStatus status;

    @Column("created_at")
    private LocalDateTime createdAt;
}
