package org.asura.code.executor.enums;

/**
 * Java代码类型枚举
 */
public enum CodeType {
    /** 完整Java类（含/不含main方法） */
    WHOLE_CLASS,
    /** 简单表达式（如a+b、user.getAge()>18） */
    EXPRESSION,
    /** 脚本代码块（多行Java代码） */
    SCRIPT
}
