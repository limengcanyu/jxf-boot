
package org.asura.ai.alibaba.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddingResponse {

    private List<List<Double>> embeddings;

    private String model;

    private Integer totalTokens;

    private Integer dimension;
}