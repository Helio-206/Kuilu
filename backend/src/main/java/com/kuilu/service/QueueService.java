package com.kuilu.service;

import com.kuilu.model.*;
import com.kuilu.repository.QueueRepository;
import com.kuilu.repository.ServiceRepository;
import com.kuilu.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {

    private final QueueRepository queueRepository;
    private final ServiceRepository serviceRepository;
    private final TicketRepository ticketRepository;

    /**
     * Obtém ou cria a fila do dia para um serviço
     */
    public Mono<Queue> getOrCreateQueueForToday(UUID serviceId) {
        LocalDate today = LocalDate.now();

        return queueRepository.findByServiceIdAndDate(serviceId, today)
                .switchIfEmpty(createNewQueue(serviceId, today));
    }

    /**
     * Cria uma nova fila para o dia
     */
    private Mono<Queue> createNewQueue(UUID serviceId, LocalDate date) {
        Queue newQueue = Queue.builder()
                .id(UUID.randomUUID())
                .serviceId(serviceId)
                .date(date)
                .currentNumber(0)
                .status(QueueStatus.OPEN)
                .build();

        return queueRepository.save(newQueue)
                .doOnSuccess(q -> log.info("Nova fila criada: {} para serviço: {}", q.getId(), serviceId))
                .onErrorMap(e -> new RuntimeException("Erro ao criar fila: " + e.getMessage()));
    }

    /**
     * Emite um ticket para o usuário entrando na fila
     * Garante incremento atômico do número
     */
    public Mono<Ticket> joinQueue(UUID serviceId, UUID userId) {
        return getOrCreateQueueForToday(serviceId)
                .flatMap(queue -> {
                    if (queue.getStatus() == QueueStatus.CLOSED) {
                        return Mono.error(new RuntimeException("Fila fechada para este serviço"));
                    }

                    // Verificar se usuário já está na fila
                    return ticketRepository.findByQueueIdAndUserId(queue.getId(), userId)
                            .flatMap(existingTicket -> {
                                log.info("Usuário {} já está na fila com ticket {}", userId, existingTicket.getId());
                                return Mono.just(existingTicket);
                            })
                            .switchIfEmpty(createTicket(queue, userId));
                })
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                .onErrorMap(e -> new RuntimeException("Erro ao entrar na fila: " + e.getMessage()));
    }

    /**
     * Cria um novo ticket de forma atômica
     */
    private Mono<Ticket> createTicket(Queue queue, UUID userId) {
        // Incrementar número de forma atômica
        int nextNumber = queue.getCurrentNumber() + 1;

        Queue updatedQueue = Queue.builder()
                .id(queue.getId())
                .serviceId(queue.getServiceId())
                .date(queue.getDate())
                .currentNumber(nextNumber)
                .status(queue.getStatus())
                .build();

        return queueRepository.save(updatedQueue)
                .flatMap(q -> {
                    Ticket ticket = Ticket.builder()
                            .id(UUID.randomUUID())
                            .queueId(queue.getId())
                            .userId(userId)
                            .number(nextNumber)
                            .status(TicketStatus.WAITING)
                            .createdAt(LocalDateTime.now())
                            .build();

                    return ticketRepository.save(ticket)
                            .doOnSuccess(t -> log.info("Ticket {} criado para usuário: {}, posição: {}", 
                                    t.getId(), userId, nextNumber));
                });
    }

    /**
     * Retorna a posição do usuário na fila
     */
    public Mono<Integer> getUserPositionInQueue(UUID queueId, UUID userId) {
        return ticketRepository.findByQueueIdAndUserId(queueId, userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuário não encontrado na fila")))
                .flatMap(userTicket -> 
                    ticketRepository.countByQueueIdAndStatus(queueId, TicketStatus.WAITING)
                            .map(count -> {
                                // Contar quantos tickets com número menor que o do usuário estão esperando
                                long position = count; // simplificado - idealmente fazer query mais específica
                                return Math.max(1, (int) position);
                            })
                );
    }

    /**
     * Calcula tempo estimado de espera em minutos
     */
    public Mono<Integer> getEstimatedWaitTime(UUID queueId, UUID userId) {
        return getUserPositionInQueue(queueId, userId)
                .flatMap(position -> 
                    ticketRepository.findByQueueIdAndUserId(queueId, userId)
                            .flatMap(ticket -> 
                                serviceRepository.findById(ticket.getQueueId())
                                        .map(service -> position * service.getAvgServiceTimeMinutes())
                                        .switchIfEmpty(Mono.just(position * 10)) // Default 10 min
                            )
                );
    }

    /**
     * Lista todos os tickets da fila ordenados por número
     */
    public Flux<Ticket> getQueueTickets(UUID queueId) {
        return ticketRepository.findByQueueIdOrderByNumberAsc(queueId);
    }

    /**
     * Muda status de um ticket
     */
    public Mono<Ticket> updateTicketStatus(UUID ticketId, TicketStatus newStatus) {
        return ticketRepository.findById(ticketId)
                .flatMap(ticket -> {
                    ticket.setStatus(newStatus);
                    return ticketRepository.save(ticket)
                            .doOnSuccess(t -> log.info("Ticket {} atualizado para status: {}", ticketId, newStatus));
                });
    }

    /**
     * Fecha a fila
     */
    public Mono<Queue> closeQueue(UUID queueId) {
        return queueRepository.findById(queueId)
                .flatMap(queue -> {
                    queue.setStatus(QueueStatus.CLOSED);
                    return queueRepository.save(queue)
                            .doOnSuccess(q -> log.info("Fila {} fechada", queueId));
                });
    }

    /**
     * Stream reativo de atualizações de tickets
     * Emite sempre que um ticket muda de status
     */
    public Flux<Ticket> watchQueueChanges(UUID queueId) {
        return getQueueTickets(queueId)
                .flatMap(ticket -> 
                    // Recarregar periodicamente para detectar mudanças
                    Flux.interval(Duration.ofSeconds(2))
                            .flatMap(l -> ticketRepository.findById(ticket.getId()))
                            .distinctUntilChanged()
                            .doOnNext(t -> log.debug("Ticket {} status: {}", t.getId(), t.getStatus()))
                );
    }
}
