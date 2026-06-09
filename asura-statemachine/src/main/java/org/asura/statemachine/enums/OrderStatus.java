package org.asura.statemachine.enums;

public enum OrderStatus {

    CREATED("CREATED", "已创建"),

    PAID("PAID", "已支付"),

    SHIPPED("SHIPPED", "已发货"),

    DELIVERED("DELIVERED", "已送达"),

    COMPLETED("COMPLETED", "已完成"),

    CANCELLED("CANCELLED", "已取消"),

    REFUNDED("REFUNDED", "已退款");

    private final String code;

    private final String description;

    OrderStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatus fromCode(String code) {
        for (OrderStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown order status code: " + code);
    }

}