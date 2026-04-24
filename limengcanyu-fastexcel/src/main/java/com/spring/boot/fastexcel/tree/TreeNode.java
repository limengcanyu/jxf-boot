package com.spring.boot.fastexcel.tree;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import java.util.Deque;
import java.util.List;

/**
 * 树节点实体类
 */
@Data
public class TreeNode {
    /** 节点唯一标识 */
    private String identity;
    private String identityChain;
    /** 父节点identity */
    private String parent;
    /** 节点名称 */
    private String name;
    /** 所属文件夹identity */
    private String folder;
    /** 节点类型（folder表示文件夹节点） */
    private String subtype;
    /** 子节点列表 */
    private List<TreeNode> children;

    // 无参构造
    public TreeNode() {}

    // 基础字段构造器（查询基础字段时使用）
    public TreeNode(String identity, String parent, String name, String folder, String subtype) {
        this.identity = identity;
        this.parent = parent;
        this.name = name;
        this.folder = folder;
        this.subtype = subtype;
    }

    public void addCodeChain(Deque<String> codeList, int maxDepth) {
        if (codeList.size() >= maxDepth) {
            throw new IllegalStateException("队列已满，最大容量: " + maxDepth);
        }

        codeList.offer(this.identity);
        this.identityChain = String.join("/", codeList);
        if (CollectionUtils.isNotEmpty(this.children)) {
            this.children.forEach(child -> child.addCodeChain(codeList, maxDepth));
        }
        codeList.pollLast();
    }

}

