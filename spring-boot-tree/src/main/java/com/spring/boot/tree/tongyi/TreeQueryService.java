package com.spring.boot.tree.tongyi;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 先查询符合条件的节点，再构成树形结构
 */
public class TreeQueryService {

    private final List<TreeNode> allNodes;
    private final Map<String, TreeNode> nodeMap;

    public TreeQueryService(List<TreeNode> allNodes) {
        this.allNodes = new ArrayList<>(allNodes);
        // 构建节点映射表，用于快速查找
        this.nodeMap = this.allNodes.stream().collect(Collectors.toMap(TreeNode::getIdentity, node -> node));
    }

    /**
     * 根据查询条件搜索并构建树形结构
     * @param criteria 查询条件
     * @return 构建好的多根节点树形结构
     */
    public List<TreeNode> searchAndBuildTree(QueryCriteria criteria) {
        if (criteria == null || !criteria.hasAnyCriteria()) {
            // 如果没有查询条件，返回完整的树结构
            return buildCompleteTree();
        }

        // 1. 执行查询，获取符合条件的节点
        List<TreeNode> matchedNodes = executeQuery(criteria);

        if (matchedNodes.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 收集所有必要的节点（匹配节点 + 必要的父节点）
        Set<String> requiredNodeIds = collectRequiredNodes(matchedNodes);

        // 3. 过滤出需要的节点
        List<TreeNode> filteredNodes = allNodes.stream()
                .filter(node -> requiredNodeIds.contains(node.getIdentity()))
                .map(this::copyNode) // 创建副本，避免修改原始数据
                .collect(Collectors.toList());

        // 4. 构建并返回树形结构
        return buildTree(filteredNodes);
    }

    /**
     * 执行查询，获取匹配的节点
     */
    private List<TreeNode> executeQuery(QueryCriteria criteria) {
        return allNodes.stream()
                .filter(node -> matchesName(node, criteria.getNameKeyword()))
                .filter(node -> matchesFolder(node, criteria.getFolderIdentity()))
                .filter(node -> matchesSubtype(node, criteria.getSubtype()))
                .collect(Collectors.toList());
    }

    /**
     * name字段模糊匹配
     */
    private boolean matchesName(TreeNode node, String nameKeyword) {
        if (nameKeyword == null || nameKeyword.trim().isEmpty()) {
            return true;
        }
        return node.getName() != null && node.getName().toLowerCase().contains(nameKeyword.toLowerCase().trim());
    }

    /**
     * 文件夹identity精确匹配
     */
    private boolean matchesFolder(TreeNode node, String folderIdentity) {
        if (folderIdentity == null || folderIdentity.trim().isEmpty()) {
            return true;
        }
        return node.getIdentity() != null && node.getIdentity().equals(folderIdentity.trim());
    }

    /**
     * subtype类型匹配
     */
    private boolean matchesSubtype(TreeNode node, String subtype) {
        if (subtype == null || subtype.trim().isEmpty()) {
            return true;
        }
        return node.getSubtype() != null && node.getSubtype().equals(subtype.trim());
    }

    /**
     * 收集所有必要的节点（匹配节点 + 必要的父节点）
     */
    private Set<String> collectRequiredNodes(List<TreeNode> matchedNodes) {
        Set<String> requiredNodeIds = new HashSet<>();

        // 添加所有匹配的节点
        matchedNodes.forEach(node -> requiredNodeIds.add(node.getIdentity()));

        // 为每个匹配节点添加其所有父节点
        for (TreeNode node : matchedNodes) {
            addParentChain(node, requiredNodeIds);
        }

        return requiredNodeIds;
    }

    /**
     * 递归添加节点的父节点链
     */
    private void addParentChain(TreeNode node, Set<String> requiredNodeIds) {
        if (node.getParent() == null || node.getParent().isEmpty()) {
            return;
        }

        TreeNode parentNode = nodeMap.get(node.getParent());
        if (parentNode != null) {
            requiredNodeIds.add(parentNode.getIdentity());
            addParentChain(parentNode, requiredNodeIds); // 递归处理更上层的父节点
        }
    }

    /**
     * 构建完整的树形结构（无查询条件时使用）
     */
    private List<TreeNode> buildCompleteTree() {
        List<TreeNode> treeNodes = allNodes.stream()
                .map(this::copyNode)
                .collect(Collectors.toList());
        return buildTree(treeNodes);
    }

    /**
     * 构建树形结构
     */
    private List<TreeNode> buildTree(List<TreeNode> nodes) {
        Map<String, TreeNode> nodeMap = nodes.stream().collect(Collectors.toMap(TreeNode::getIdentity, node -> node));

        List<TreeNode> rootNodes = new ArrayList<>();

        for (TreeNode node : nodes) {
            if (node.getParent() == null || node.getParent().isEmpty()) {
                rootNodes.add(node);
            } else {
                TreeNode parentNode = nodeMap.get(node.getParent());
                if (parentNode != null) {
                    if (parentNode.getChildren() == null) {
                        parentNode.setChildren(new ArrayList<>());
                    }
                    parentNode.getChildren().add(node);
                }
            }
        }

        // 按name排序，使结果更有序
        rootNodes.sort(Comparator.comparing(TreeNode::getName));

        return rootNodes;
    }

    /**
     * 复制节点，避免修改原始数据
     */
    private TreeNode copyNode(TreeNode original) {
        TreeNode copy = new TreeNode();
        copy.setIdentity(original.getIdentity());
        copy.setName(original.getName());
        copy.setSubtype(original.getSubtype());
        copy.setParent(original.getParent());
        copy.setChildren(new ArrayList<>());
        return copy;
    }
}
