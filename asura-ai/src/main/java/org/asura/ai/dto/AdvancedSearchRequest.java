package org.asura.ai.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdvancedSearchRequest {
    private String keyword;
    private Long categoryId;
    private String author;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
