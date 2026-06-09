
package org.asura.ai.alibaba.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddingRequest {

    @NotEmpty(message = "文本列表不能为空")
    private List<String> texts;

    private String model = "text-embedding-v1";
}