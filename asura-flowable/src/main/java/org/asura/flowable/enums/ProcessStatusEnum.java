package org.asura.flowable.enums;

import lombok.Getter;

/**
 * 流程状态枚举
 */
@Getter
public enum ProcessStatusEnum {
    
    RUNNING("running", "运行中"),
    COMPLETED("completed", "已完成"),
    SUSPENDED("suspended", "已挂起"),
    TERMINATED("terminated", "已终止");

    private final String code;
    private final String desc;

    ProcessStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ProcessStatusEnum fromCode(String code) {
        for (ProcessStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}