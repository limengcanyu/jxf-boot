package io.github.limengcanyu.constant;

public class OperationalTypeConst {
    public static final int OPERATIONAL_TYPE_KEY_ADD = 1;
    public static final String OPERATIONAL_TYPE_VALUE_ADD = "新增";

    public static final int OPERATIONAL_TYPE_KEY_UPDATE = 2;
    public static final String OPERATIONAL_TYPE_VALUE_UPDATE = "更新";

    public static String getOperationalTypeName(int key) {
        switch (key) {
            case 1 -> {
                return OPERATIONAL_TYPE_VALUE_ADD;
            }
            case 2 -> {
                return OPERATIONAL_TYPE_VALUE_UPDATE;
            }
            default -> {
                return "";
            }
        }
    }
}
