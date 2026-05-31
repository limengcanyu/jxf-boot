package org.asura.sse.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SseEvent {

    private String id;
    
    private String eventType;
    
    private String data;
    
    private String retry;
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}