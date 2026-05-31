package org.asura.sse.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.asura.sse.model.SseMessageRequest;
import org.asura.sse.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sse")
public class SseController {

    private final SseService sseService;

    @Autowired
    public SseController(SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "channel", required = false) String channel) {
        
        if (clientId == null || clientId.isEmpty()) {
            clientId = "anonymous-" + System.currentTimeMillis();
        }
        
        return sseService.createConnection(clientId, channel);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, String>> subscribe(
            @RequestParam String sessionId,
            @RequestParam String channel) {
        
        sseService.subscribeChannel(sessionId, channel);
        return ResponseEntity.ok(Map.of("message", "Subscribed to channel: " + channel));
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<Map<String, String>> unsubscribe(
            @RequestParam String sessionId,
            @RequestParam String channel) {
        
        sseService.unsubscribeChannel(sessionId, channel);
        return ResponseEntity.ok(Map.of("message", "Unsubscribed from channel: " + channel));
    }

    @PostMapping("/broadcast")
    public ResponseEntity<Map<String, String>> broadcast(
            @Valid @RequestBody SseMessageRequest request) {
        
        log.info("Received broadcast request: channel={}, eventType={}", 
                request.getChannel(), request.getEventType());
        
        sseService.broadcast(request);
        return ResponseEntity.ok(Map.of("message", "Broadcast message sent"));
    }

    @PostMapping("/send/{clientId}")
    public ResponseEntity<Map<String, String>> sendToClient(
            @PathVariable String clientId,
            @Valid @RequestBody SseMessageRequest request) {
        
        log.info("Sending message to client: clientId={}", clientId);
        
        sseService.sendToClient(clientId, request);
        return ResponseEntity.ok(Map.of("message", "Message sent to client"));
    }

    @PostMapping("/send/session/{sessionId}")
    public ResponseEntity<Map<String, String>> sendToSession(
            @PathVariable String sessionId,
            @Valid @RequestBody SseMessageRequest request) {
        
        log.info("Sending message to session: sessionId={}", sessionId);
        
        sseService.sendToSession(sessionId, request);
        return ResponseEntity.ok(Map.of("message", "Message sent to session"));
    }

    @PostMapping("/disconnect")
    public ResponseEntity<Map<String, String>> disconnect(
            @RequestParam String sessionId) {
        
        log.info("Client disconnecting: sessionId={}", sessionId);
        sseService.disconnect(sessionId);
        return ResponseEntity.ok(Map.of("message", "Disconnected successfully"));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(Map.of(
                "activeConnections", sseService.getActiveConnectionsCount(),
                "timestamp", System.currentTimeMillis()
        ));
    }
}