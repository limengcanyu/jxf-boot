package org.asura.restful.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "创建订单请求")
public class OrderCreateRequest {

    @NotBlank(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    @NotNull(message = "订单金额不能为空")
    @DecimalMin(value = "0.01", message = "订单金额必须大于0")
    @Schema(description = "订单金额", example = "99.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalAmount;

    @Schema(description = "备注", example = "加急订单")
    private String remark;

    public OrderCreateRequest() {}

    public OrderCreateRequest(String userId, BigDecimal totalAmount, String remark) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.remark = remark;
    }

    public static OrderCreateRequestBuilder builder() {
        return new OrderCreateRequestBuilder();
    }

    public static class OrderCreateRequestBuilder {
        private String userId;
        private BigDecimal totalAmount;
        private String remark;

        public OrderCreateRequestBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public OrderCreateRequestBuilder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public OrderCreateRequestBuilder remark(String remark) {
            this.remark = remark;
            return this;
        }

        public OrderCreateRequest build() {
            return new OrderCreateRequest(userId, totalAmount, remark);
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}