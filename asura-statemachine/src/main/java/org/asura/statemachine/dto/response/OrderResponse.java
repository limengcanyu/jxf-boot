package org.asura.statemachine.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class OrderResponse {

    private String orderId;

    private String orderNo;

    private String status;

    private String statusDescription;

    private String createdBy;

    private LocalDateTime createdTime;

    private String updatedBy;

    private LocalDateTime updatedTime;

    private String remark;

}