
package org.asura.ai.alibaba.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    @NotBlank(message = "消息内容不能为空")
    private String message;

    private String model = "qwen-max";

    private Double temperature = 0.7;

    private Integer maxTokens = 2048;

    private String conversationId;
}