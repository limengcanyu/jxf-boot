package org.asura.statemachine.util;

import org.asura.statemachine.enums.OrderStatus;

public final class OrderStatusUtil {

    private OrderStatusUtil() {
    }

    public static String getStatusDescription(String statusCode) {
        try {
            OrderStatus status = OrderStatus.fromCode(statusCode);
            return status.getDescription();
        } catch (IllegalArgumentException e) {
            return "未知状态";
        }
    }

    public static boolean isValidStatus(String statusCode) {
        try {
            OrderStatus.fromCode(statusCode);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}