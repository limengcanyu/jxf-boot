package org.asura.sse.service;

import lombok.extern.slf4j.Slf4j;
import org.asura.sse.model.SseEvent;
import org.asura.sse.model.SseMessageRequest;
import org.asura.sse.model.SseSession;
import org.asura.sse.repository.SseSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class SseService {

    private static final long SSE_TIMEOUT = 300000L;

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    
    private final SseSessionRepository sessionRepository;

    @Autowired
    public SseService(SseSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public SseEmitter createConnection(String clientId, String channel) {
        String sessionId = UUID.randomUUID().toString();
        
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        
        SseSession session = SseSession.builder()
                .sessionId(sessionId)
                .clientId(clientId)
                .emitter(emitter)
                .createdAt(LocalDateTime.now())
                .lastActiveAt(LocalDateTime.now())
                .build();

        if (channel != null && !channel.isEmpty()) {
            session.getChannels().add(channel);
        }

        sessionRepository.save(session);

        emitter.onCompletion(() -> handleCompletion(sessionId));
        emitter.onTimeout(() -> handleTimeout(sessionId));
        emitter.onError(e -> handleError(sessionId, e));

        executorService.submit(() -> {
            try {
                emitter.send(SseEmitter.event()
                        .id(sessionId)
                        .name("connected")
                        .data("{\"sessionId\":\"" + sessionId + "\",\"clientId\":\"" + clientId + "\",\"message\":\"Connected successfully\"}"));
                log.info("Sent welcome message to session: {}", sessionId);
            } catch (IOException e) {
                log.warn("Failed to send welcome message: {}", e.getMessage());
                cleanupSession(sessionId);
            }
        });

        log.info("SSE connection created: sessionId={}, clientId={}, channel={}", 
                sessionId, clientId, channel);

        return emitter;
    }

    public void subscribeChannel(String sessionId, String channel) {
        SseSession session = sessionRepository.findById(sessionId);
        if (session != null) {
            session.getChannels().add(channel);
            session.setLastActiveAt(LocalDateTime.now());
            log.info("Session {} subscribed to channel {}", sessionId, channel);
        }
    }

    public void unsubscribeChannel(String sessionId, String channel) {
        SseSession session = sessionRepository.findById(sessionId);
        if (session != null) {
            session.getChannels().remove(channel);
            log.info("Session {} unsubscribed from channel {}", sessionId, channel);
        }
    }

    public void broadcast(SseMessageRequest request) {
        String channel = request.getChannel();
        Collection<SseSession> sessions;

        if (channel != null && !channel.isEmpty()) {
            sessions = sessionRepository.findByChannel(channel);
        } else {
            sessions = sessionRepository.findAll();
        }

        SseEvent event = SseEvent.builder()
                .id(UUID.randomUUID().toString())
                .eventType(request.getEventType())
                .data(request.getData())
                .timestamp(LocalDateTime.now())
                .build();

        sendToSessions(sessions, event);
    }

    public void sendToClient(String clientId, SseMessageRequest request) {
        Collection<SseSession> sessions = sessionRepository.findByClientId(clientId);
        
        SseEvent event = SseEvent.builder()
                .id(UUID.randomUUID().toString())
                .eventType(request.getEventType())
                .data(request.getData())
                .timestamp(LocalDateTime.now())
                .build();

        sendToSessions(sessions, event);
    }

    public void sendToSession(String sessionId, SseMessageRequest request) {
        SseSession session = sessionRepository.findById(sessionId);
        if (session != null) {
            SseEvent event = SseEvent.builder()
                    .id(UUID.randomUUID().toString())
                    .eventType(request.getEventType())
                    .data(request.getData())
                    .timestamp(LocalDateTime.now())
                    .build();

            sendEvent(session, event);
        }
    }

    private void sendToSessions(Collection<SseSession> sessions, SseEvent event) {
        sessions.forEach(session -> executorService.submit(() -> sendEvent(session, event)));
        log.info("Sending event to {} sessions", sessions.size());
    }

    private void sendEvent(SseSession session, SseEvent event) {
        try {
            SseEmitter.SseEventBuilder builder = SseEmitter.event()
                    .id(event.getId())
                    .data(event.getData());

            if (event.getEventType() != null && !event.getEventType().isEmpty()) {
                builder.name(event.getEventType());
            }

            session.getEmitter().send(builder.build());
            session.setLastActiveAt(LocalDateTime.now());
        } catch (IOException e) {
            log.warn("Failed to send event to session {}: {}", session.getSessionId(), e.getMessage());
            cleanupSession(session.getSessionId());
        }
    }

    private void handleCompletion(String sessionId) {
        log.info("SSE connection completed: sessionId={}", sessionId);
        cleanupSession(sessionId);
    }

    private void handleTimeout(String sessionId) {
        log.warn("SSE connection timeout: sessionId={}", sessionId);
        cleanupSession(sessionId);
    }

    private void handleError(String sessionId, Throwable e) {
        log.error("SSE connection error: sessionId={}, error={}", sessionId, e.getMessage());
        cleanupSession(sessionId);
    }

    private void cleanupSession(String sessionId) {
        SseSession session = sessionRepository.findById(sessionId);
        if (session != null) {
            try {
                session.getEmitter().complete();
            } catch (Exception e) {
                log.debug("Error completing emitter: {}", e.getMessage());
            }
            sessionRepository.deleteById(sessionId);
            log.info("SSE session cleaned up: sessionId={}", sessionId);
        }
    }

    public long getActiveConnectionsCount() {
        return sessionRepository.count();
    }

    public void disconnect(String sessionId) {
        cleanupSession(sessionId);
    }
}