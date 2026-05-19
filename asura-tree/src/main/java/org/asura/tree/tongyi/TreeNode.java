package org.asura.tree.tongyi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode {
    private String identity;
    private String name;
    private String subtype;
    private String parent;
    private List<TreeNode> children = new ArrayList<>();

    // 为了便于调试和日志输出
    @Override
    public String toString() {
        return String.format("TreeNode{identity='%s', name='%s', subtype='%s', parent='%s'}",
                identity, name, subtype, parent);
    }
}
