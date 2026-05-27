
package org.asura.ai.alibaba.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationMemory {
    private String conversationId;
    private String role;
    private String content;
    private LocalDateTime createdAt;
}