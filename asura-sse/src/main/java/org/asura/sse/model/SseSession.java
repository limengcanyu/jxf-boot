package org.asura.sse.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SseSession {

    private String sessionId;
    
    private String clientId;
    
    private SseEmitter emitter;
    
    @Builder.Default
    private Set<String> channels = new CopyOnWriteArraySet<>();
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Builder.Default
    private LocalDateTime lastActiveAt = LocalDateTime.now();
    
    private String remoteAddress;
    
    private String userAgent;
}