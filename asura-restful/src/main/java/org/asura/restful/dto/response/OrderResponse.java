package org.asura.restful.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "订单响应")
public class OrderResponse {

    @Schema(description = "订单ID")
    private String id;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "订单金额")
    private BigDecimal totalAmount;

    @Schema(description = "订单状态")
    private Integer status;

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    public OrderResponse() {}

    public OrderResponse(String id, String userId, String orderNo, BigDecimal totalAmount,
                         Integer status, String statusDesc, String remark,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.orderNo = orderNo;
        this.totalAmount = totalAmount;
        this.status = status;
        this.statusDesc = statusDesc;
        this.remark = remark;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static OrderResponseBuilder builder() {
        return new OrderResponseBuilder();
    }

    public static class OrderResponseBuilder {
        private String id;
        private String userId;
        private String orderNo;
        private BigDecimal totalAmount;
        private Integer status;
        private String statusDesc;
        private String remark;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public OrderResponseBuilder id(String id) {
            this.id = id;
            return this;
        }

        public OrderResponseBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public OrderResponseBuilder orderNo(String orderNo) {
            this.orderNo = orderNo;
            return this;
        }

        public OrderResponseBuilder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public OrderResponseBuilder status(Integer status) {
            this.status = status;
            return this;
        }

        public OrderResponseBuilder statusDesc(String statusDesc) {
            this.statusDesc = statusDesc;
            return this;
        }

        public OrderResponseBuilder remark(String remark) {
            this.remark = remark;
            return this;
        }

        public OrderResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public OrderResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public OrderResponse build() {
            return new OrderResponse(id, userId, orderNo, totalAmount, status, statusDesc, remark, createdAt, updatedAt);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}