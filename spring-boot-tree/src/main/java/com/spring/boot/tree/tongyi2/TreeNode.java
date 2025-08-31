package com.spring.boot.tree.tongyi2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 树节点实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class TreeNode {
    private String identity;
    private String name;
    private String subtype;
    private String parent;
    private List<TreeNode> children = new ArrayList<>();
    private boolean matched = false; // 标记是否匹配查询条件

    public TreeNode(String identity, String name, String subtype, String parent) {
        this.identity = identity;
        this.name = name;
        this.subtype = subtype;
        this.parent = parent;
    }

    @Override
    public String toString() {
        return String.format("TreeNode{identity='%s', name='%s', subtype='%s', parent='%s'}",
                identity, name, subtype, parent);
    }
}
