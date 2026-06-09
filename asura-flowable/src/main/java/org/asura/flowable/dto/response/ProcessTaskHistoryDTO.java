package org.asura.flowable.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ProcessTaskHistoryDTO {

    private String taskId;
    private String taskName;
    private String taskKey;
    private String assignee;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationInMillis;
    private String comment;
    private String actionResult;

}