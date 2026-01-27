package com.kuilu.controller;

import com.kuilu.model.Queue;
import com.kuilu.model.Ticket;
import com.kuilu.model.TicketStatus;
import com.kuilu.service.QueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/queues")
@RequiredArgsConstructor
@Slf4j
public class QueueController {

    private final QueueService queueService;

    /**
     * Entra na fila de um serviço
     * POST /api/queues/join?serviceId={id}&userId={id}
     */
    @PostMapping("/join")
    public Mono<ResponseEntity<Ticket>> joinQueue(
            @RequestParam UUID serviceId,
            @RequestParam UUID userId) {
        
        log.info("Usuário {} entrando na fila do serviço {}", userId, serviceId);
        
        return queueService.joinQueue(serviceId, userId)
                .map(ticket -> ResponseEntity.status(HttpStatus.CREATED).body(ticket))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    /**
     * Retorna informações da posição do usuário na fila
     * GET /api/queues/{queueId}/position?userId={id}
     */
    @GetMapping("/{queueId}/position")
    public Mono<ResponseEntity<Map<String, Object>>> getUserPosition(
            @PathVariable UUID queueId,
            @RequestParam UUID userId) {
        
        return queueService.getUserPositionInQueue(queueId, userId)
                .zipWith(queueService.getEstimatedWaitTime(queueId, userId))
                .map(tuple -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("position", tuple.getT1());
                    response.put("estimatedWaitTimeMinutes", tuple.getT2());
                    return ResponseEntity.ok(response);
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Stream SSE para acompanhar a fila em tempo real
     * GET /api/queues/{queueId}/stream
     */
    @GetMapping(value = "/{queueId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Ticket> streamQueueUpdates(@PathVariable UUID queueId) {
        log.info("Cliente conectado ao stream da fila: {}", queueId);
        
        return queueService.watchQueueChanges(queueId)
                .doOnCancel(() -> log.info("Cliente desconectado do stream: {}", queueId))
                .doOnError(e -> log.error("Erro no stream da fila {}: {}", queueId, e.getMessage()));
    }

    /**
     * Retorna todos os tickets da fila
     * GET /api/queues/{queueId}/tickets
     */
    @GetMapping("/{queueId}/tickets")
    public Flux<Ticket> getQueueTickets(@PathVariable UUID queueId) {
        return queueService.getQueueTickets(queueId);
    }

    /**
     * Atualiza o status de um ticket
     * PUT /api/queues/tickets/{ticketId}/status
     */
    @PutMapping("/tickets/{ticketId}/status")
    public Mono<ResponseEntity<Ticket>> updateTicketStatus(
            @PathVariable UUID ticketId,
            @RequestParam TicketStatus status) {
        
        log.info("Atualizando ticket {} para status: {}", ticketId, status);
        
        return queueService.updateTicketStatus(ticketId, status)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Fecha a fila
     * PUT /api/queues/{queueId}/close
     */
    @PutMapping("/{queueId}/close")
    public Mono<ResponseEntity<Queue>> closeQueue(@PathVariable UUID queueId) {
        log.info("Fechando fila: {}", queueId);
        
        return queueService.closeQueue(queueId)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
