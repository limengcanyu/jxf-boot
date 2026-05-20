package org.asura.flowable.enums;

import lombok.Getter;

/**
 * 任务操作枚举
 */
@Getter
public enum TaskActionEnum {
    
    APPROVE("approve", "审批通过"),
    REJECT("reject", "审批拒绝"),
    DELEGATE("delegate", "委派"),
    ASSIGN("assign", "转办"),
    CLAIM("claim", "签收"),
    UNCLAIM("unclaim", "取消签收");

    private final String code;
    private final String desc;

    TaskActionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TaskActionEnum fromCode(String code) {
        for (TaskActionEnum action : values()) {
            if (action.code.equals(code)) {
                return action;
            }
        }
        return null;
    }
}