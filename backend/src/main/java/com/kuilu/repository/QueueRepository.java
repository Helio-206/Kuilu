package com.kuilu.repository;

import com.kuilu.model.Queue;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface QueueRepository extends ReactiveCrudRepository<Queue, UUID> {

    @Query("SELECT * FROM queues WHERE service_id = :serviceId AND date = :date LIMIT 1")
    Mono<Queue> findByServiceIdAndDate(UUID serviceId, LocalDate date);
}
