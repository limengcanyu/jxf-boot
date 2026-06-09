package org.asura.sse.repository;

import org.asura.sse.model.SseSession;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class SseSessionRepository {

    private final Map<String, SseSession> sessionStore = new ConcurrentHashMap<>();

    public void save(SseSession session) {
        sessionStore.put(session.getSessionId(), session);
    }

    public SseSession findById(String sessionId) {
        return sessionStore.get(sessionId);
    }

    public void deleteById(String sessionId) {
        sessionStore.remove(sessionId);
    }

    public Collection<SseSession> findAll() {
        return sessionStore.values();
    }

    public Collection<SseSession> findByChannel(String channel) {
        return sessionStore.values().stream()
                .filter(session -> session.getChannels().contains(channel))
                .collect(Collectors.toList());
    }

    public Collection<SseSession> findByClientId(String clientId) {
        return sessionStore.values().stream()
                .filter(session -> clientId.equals(session.getClientId()))
                .collect(Collectors.toList());
    }

    public long count() {
        return sessionStore.size();
    }

    public void clear() {
        sessionStore.clear();
    }
}