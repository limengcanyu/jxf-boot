package com.spring.boot.tree.tongyi2;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 先构建树形结构，再查询符合条件的节点
 */
class TreeStructureService {

    private List<TreeNode> completeTree; // 完整的多根节点树
    private Map<String, TreeNode> nodeMap; // 节点映射表

    public TreeStructureService(List<TreeNode> allNodes) {
        // 构建节点映射表
        this.nodeMap = allNodes.stream().collect(Collectors.toMap(TreeNode::getIdentity, node -> node));

        // 构建完整的树形结构
        this.completeTree = buildCompleteTree(allNodes);
    }

    /**
     * 构建完整的多根节点树形结构
     */
    private List<TreeNode> buildCompleteTree(List<TreeNode> nodes) {
        // 创建节点副本，避免修改原始数据
        List<TreeNode> treeNodes = nodes.stream().map(this::copyNode).toList();

        Map<String, TreeNode> nodeMap = treeNodes.stream().collect(Collectors.toMap(TreeNode::getIdentity, node -> node));

        List<TreeNode> rootNodes = new ArrayList<>();

        for (TreeNode node : treeNodes) {
            if (node.getParent() == null || node.getParent().isEmpty()) {
                rootNodes.add(node);
            } else {
                TreeNode parentNode = nodeMap.get(node.getParent());
                if (parentNode != null) {
                    parentNode.getChildren().add(node);
                }
            }
        }

        // 按名称排序
        rootNodes.sort(Comparator.comparing(TreeNode::getName));
        return rootNodes;
    }

    /**
     * 在完整树结构上执行查询
     */
    public List<TreeNode> queryInTree(QueryCriteria criteria) {
        if (criteria == null || !criteria.hasAnyCriteria()) {
            // 无查询条件时，返回完整的树（重置matched标记）
            resetMatchedFlags(completeTree);
            return deepCopyTree(completeTree);
        }

        // 1. 重置所有节点的matched标记
        resetMatchedFlags(completeTree);

        // 2. 在树中执行查询，标记匹配的节点
        List<TreeNode> matchedNodes = findAndMarkMatches(completeTree, criteria);

        // 3. 确保匹配节点的父节点链也被包含
        ensureParentChain(matchedNodes);

        // 4. 过滤树结构，只保留匹配的节点及其必要的父节点
        return filterTree(completeTree);
    }

    /**
     * 重置所有节点的matched标记
     */
    private void resetMatchedFlags(List<TreeNode> tree) {
        for (TreeNode node : tree) {
            node.setMatched(false);
            if (!node.getChildren().isEmpty()) {
                resetMatchedFlags(node.getChildren());
            }
        }
    }

    /**
     * 查找并标记匹配的节点
     */
    private List<TreeNode> findAndMarkMatches(List<TreeNode> tree, QueryCriteria criteria) {
        List<TreeNode> matchedNodes = new ArrayList<>();

        for (TreeNode node : tree) {
            if (matchesCriteria(node, criteria)) {
                node.setMatched(true);
                matchedNodes.add(node);
            }

            // 递归处理子节点
            if (!node.getChildren().isEmpty()) {
                matchedNodes.addAll(findAndMarkMatches(node.getChildren(), criteria));
            }
        }

        return matchedNodes;
    }

    /**
     * 检查节点是否匹配查询条件
     */
    private boolean matchesCriteria(TreeNode node, QueryCriteria criteria) {
        return matchesName(node, criteria.getNameKeyword()) &&
                matchesFolder(node, criteria.getFolderIdentity()) &&
                matchesSubtype(node, criteria.getSubtype());
    }

    /**
     * name字段模糊匹配
     */
    private boolean matchesName(TreeNode node, String nameKeyword) {
        if (nameKeyword == null || nameKeyword.trim().isEmpty()) {
            return true; // 条件为空，不筛选
        }
        return node.getName() != null &&
                node.getName().toLowerCase().contains(nameKeyword.toLowerCase().trim());
    }

    /**
     * 文件夹identity精确匹配
     */
    private boolean matchesFolder(TreeNode node, String folderIdentity) {
        if (folderIdentity == null || folderIdentity.trim().isEmpty()) {
            return true; // 条件为空，不筛选
        }
        return node.getIdentity() != null && node.getIdentity().equals(folderIdentity.trim());
    }

    /**
     * subtype类型匹配
     */
    private boolean matchesSubtype(TreeNode node, String subtype) {
        if (subtype == null || subtype.trim().isEmpty()) {
            return true; // 条件为空，不筛选
        }
        return node.getSubtype() != null && node.getSubtype().equals(subtype.trim());
    }

    /**
     * 确保匹配节点的父节点链也被包含
     */
    private void ensureParentChain(List<TreeNode> matchedNodes) {
        for (TreeNode node : matchedNodes) {
            markParentChain(node);
        }
    }

    /**
     * 标记从节点到根节点的整个父节点链
     */
    private void markParentChain(TreeNode node) {
        TreeNode current = node;
        while (current.getParent() != null && !current.getParent().isEmpty()) {
            TreeNode parentNode = findNodeByIdentity(current.getParent());
            if (parentNode != null) {
                parentNode.setMatched(true);
                current = parentNode;
            } else {
                break;
            }
        }
    }

    /**
     * 根据identity查找节点
     */
    private TreeNode findNodeByIdentity(String identity) {
        return flattenTree(completeTree).filter(node -> node.getIdentity().equals(identity))
                .findFirst()
                .orElse(null);
    }

    /**
     * 将树结构展平为流
     */
    private Stream<TreeNode> flattenTree(List<TreeNode> tree) {
        return tree.stream().flatMap(this::flattenNode);
    }

    private Stream<TreeNode> flattenNode(TreeNode node) {
        return Stream.concat(
                Stream.of(node),
                node.getChildren().stream().flatMap(this::flattenNode)
        );
    }

    /**
     * 过滤树结构，只保留matched为true的节点
     */
    private List<TreeNode> filterTree(List<TreeNode> sourceTree) {
        List<TreeNode> result = new ArrayList<>();

        for (TreeNode node : sourceTree) {
            TreeNode filteredNode = filterNode(node);
            if (filteredNode != null) {
                result.add(filteredNode);
            }
        }

        return result;
    }

    /**
     * 过滤单个节点及其子树
     */
    private TreeNode filterNode(TreeNode node) {
        if (!node.isMatched()) {
            return null;
        }

        TreeNode filteredNode = copyNode(node);
        filteredNode.setMatched(false); // 清除标记，避免影响后续查询

        // 过滤子节点
        List<TreeNode> filteredChildren = node.getChildren().stream()
                .map(this::filterNode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        filteredNode.setChildren(filteredChildren);
        return filteredNode;
    }

    /**
     * 创建节点副本
     */
    private TreeNode copyNode(TreeNode original) {
        TreeNode copy = new TreeNode();
        copy.setIdentity(original.getIdentity());
        copy.setName(original.getName());
        copy.setSubtype(original.getSubtype());
        copy.setParent(original.getParent());
        copy.setChildren(new ArrayList<>());
        copy.setMatched(original.isMatched());
        return copy;
    }

    /**
     * 深度复制树结构
     */
    private List<TreeNode> deepCopyTree(List<TreeNode> source) {
        return source.stream()
                .map(this::deepCopyNode)
                .collect(Collectors.toList());
    }

    private TreeNode deepCopyNode(TreeNode node) {
        TreeNode copy = copyNode(node);
        copy.setChildren(node.getChildren().stream()
                .map(this::deepCopyNode)
                .collect(Collectors.toList()));
        return copy;
    }
}
