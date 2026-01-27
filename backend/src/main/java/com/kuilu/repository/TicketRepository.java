package com.kuilu.repository;

import com.kuilu.model.Ticket;
import com.kuilu.model.TicketStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface TicketRepository extends ReactiveCrudRepository<Ticket, UUID> {

    @Query("SELECT COUNT(*) FROM tickets WHERE queue_id = :queueId AND status = :status")
    Mono<Long> countByQueueIdAndStatus(UUID queueId, TicketStatus status);

    @Query("SELECT * FROM tickets WHERE queue_id = :queueId ORDER BY number ASC")
    Flux<Ticket> findByQueueIdOrderByNumberAsc(UUID queueId);

    @Query("SELECT * FROM tickets WHERE queue_id = :queueId AND user_id = :userId LIMIT 1")
    Mono<Ticket> findByQueueIdAndUserId(UUID queueId, UUID userId);
}
