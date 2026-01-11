package com.spring.boot.fastexcel.tree;

import lombok.Data;

/**
 * 树节点查询条件
 */
@Data
public class TreeNodeQuery {
    /** 名称包含匹配 */
    private String name;
    /** 文件夹范围（查询该文件夹下所有节点） */
    private String folder;
    /** 节点类型匹配 */
    private String subtype;
}
