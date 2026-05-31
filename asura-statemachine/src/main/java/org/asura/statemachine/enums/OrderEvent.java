package org.asura.statemachine.enums;

public enum OrderEvent {

    PAY("PAY", "支付"),

    SHIP("SHIP", "发货"),

    DELIVER("DELIVER", "送达"),

    COMPLETE("COMPLETE", "完成"),

    CANCEL("CANCEL", "取消"),

    REFUND("REFUND", "退款"),

    REVERT("REVERT", "回退");

    private final String code;

    private final String description;

    OrderEvent(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrderEvent fromCode(String code) {
        for (OrderEvent event : values()) {
            if (event.code.equals(code)) {
                return event;
            }
        }
        throw new IllegalArgumentException("Unknown order event code: " + code);
    }

}