package org.asura.fastexcel.tree;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 树节点核心服务（优化父节点递归终止逻辑）
 */
@Service
public class TreeNodeService {

    /**
     * 模拟数据库DAO层 - 查询所有节点（基础字段）
     */
    private List<TreeNode> queryAllNodesBasicFields() {
        // 模拟测试数据
        List<TreeNode> nodes = new ArrayList<>();
        // 根文件夹1
        nodes.add(new TreeNode("folder1", "", "文件夹1", "folder1", "folder"));
        // 文件夹1下的文件1
        nodes.add(new TreeNode("file1", "folder1", "测试文件1", "folder1", "file"));
        // 文件夹1下的文件2
        nodes.add(new TreeNode("file2", "folder1", "数据文件1", "folder1", "file"));
        // 根文件夹2
        nodes.add(new TreeNode("folder2", "", "文件夹2", "folder2", "folder"));
        // 文件夹2下的文件3
        nodes.add(new TreeNode("file3", "folder2", "测试文件2", "folder2", "file"));
        // 文件夹2下的子文件夹
        nodes.add(new TreeNode("folder3", "folder2", "子文件夹1", "folder2", "folder"));
        // 子文件夹1下的文件4
        nodes.add(new TreeNode("file4", "folder3", "测试数据1", "folder2", "file"));
        // 根文件夹3（用于测试多层递归终止）
        nodes.add(new TreeNode("folder4", "", "文件夹4", "folder4", "folder"));
        nodes.add(new TreeNode("folder5", "folder4", "子文件夹2", "folder4", "folder"));
        nodes.add(new TreeNode("file5", "folder5", "测试文件5", "folder4", "file"));
        return nodes;
    }

    /**
     * 模拟数据库DAO层 - 根据identity集合查询完整节点信息
     */
    private List<TreeNode> queryNodesByIds(Set<String> identities) {
        return queryAllNodesBasicFields().stream()
                .filter(node -> identities.contains(node.getIdentity()))
                .collect(Collectors.toList());
    }

    /**
     * 核心方法：根据条件生成树结构
     */
    public List<TreeNode> generateTree(TreeNodeQuery query) {
        // 步骤1：查询所有节点（基础字段）
        List<TreeNode> allNodes = queryAllNodesBasicFields();

        // 步骤2：过滤folder下所有节点，排除文件夹节点（subtype≠folder）
        List<TreeNode> result1 = filterByFolder(allNodes, query.getFolder());

        // 步骤3：过滤subtype匹配的节点
        List<TreeNode> result2 = filterBySubtype(result1, query.getSubtype());

        // 步骤4：过滤name包含的节点
        List<TreeNode> result3 = filterByName(result2, query.getName());

        // 步骤5：递归获取所有父节点（folder有值时递归到folder节点终止），生成完整节点ID集合
        Set<String> allRelatedIds = getAllRelatedParentIds(result3, allNodes, query.getFolder());

        // 步骤6：根据ID集合查询完整节点信息
        List<TreeNode> result5 = queryNodesByIds(allRelatedIds);

        // 步骤7：生成树结构
        return buildTree(result5);
    }

    /**
     * 步骤2实现：根据folder过滤节点（递归获取所有子节点），排除文件夹节点
     */
    private List<TreeNode> filterByFolder(List<TreeNode> allNodes, String folderParam) {
        // folder参数为空时，返回所有非文件夹节点
        if (folderParam == null || folderParam.isBlank()) {
            return allNodes.stream()
                    .filter(node -> !"folder".equals(node.getSubtype()))
                    .collect(Collectors.toList());
        }

        // 递归获取folderParam下所有子节点ID
        Set<String> folderChildIds = new HashSet<>();
        collectFolderChildren(allNodes, folderParam, folderChildIds);

        // 过滤出子节点，且排除文件夹节点
        return allNodes.stream()
                .filter(node -> folderChildIds.contains(node.getIdentity()))
                .filter(node -> !"folder".equals(node.getSubtype()))
                .collect(Collectors.toList());
    }

    /**
     * 递归收集文件夹下所有子节点ID（包括多层子节点）
     */
    private void collectFolderChildren(List<TreeNode> allNodes, String parentId, Set<String> childIds) {
        // 查找当前父节点的直接子节点
        List<TreeNode> directChildren = allNodes.stream()
                .filter(node -> parentId.equals(node.getParent()))
                .toList();

        for (TreeNode child : directChildren) {
            childIds.add(child.getIdentity());
            // 递归收集子节点的子节点（无论是否文件夹）
            collectFolderChildren(allNodes, child.getIdentity(), childIds);
        }
    }

    /**
     * 步骤3实现：根据subtype过滤节点
     */
    private List<TreeNode> filterBySubtype(List<TreeNode> nodes, String subtypeParam) {
        if (subtypeParam == null || subtypeParam.isBlank()) {
            return new ArrayList<>(nodes);
        }
        return nodes.stream()
                .filter(node -> subtypeParam.equals(node.getSubtype()))
                .collect(Collectors.toList());
    }

    /**
     * 步骤4实现：根据name包含过滤节点
     */
    private List<TreeNode> filterByName(List<TreeNode> nodes, String nameParam) {
        if (nameParam == null || nameParam.isBlank()) {
            return new ArrayList<>(nodes);
        }
        String lowerName = nameParam.toLowerCase();
        return nodes.stream()
                .filter(node -> node.getName() != null && node.getName().toLowerCase().contains(lowerName))
                .collect(Collectors.toList());
    }

    /**
     * 步骤5优化：递归获取所有相关父节点ID（包括自身）
     * 关键变更：folder参数有值时，递归到folder节点为止，不再向上递归
     */
    private Set<String> getAllRelatedParentIds(List<TreeNode> targetNodes, List<TreeNode> allNodes, String folderParam) {
        Set<String> relatedIds = new HashSet<>();
        // 先添加目标节点自身ID
        targetNodes.forEach(node -> relatedIds.add(node.getIdentity()));

        // 递归添加所有父节点ID（带folder终止逻辑）
        for (TreeNode node : targetNodes) {
            collectParentIdsWithTermination(node.getIdentity(), allNodes, relatedIds, folderParam);
        }
        return relatedIds;
    }

    /**
     * 递归收集单个节点的所有父节点ID（核心优化：folder有值时递归到该节点终止）
     * @param nodeId 当前节点ID
     * @param allNodes 所有节点列表
     * @param relatedIds 收集的ID集合（自动去重）
     * @param folderParam folder查询参数（终止节点）
     */
    private void collectParentIdsWithTermination(String nodeId, List<TreeNode> allNodes, Set<String> relatedIds, String folderParam) {
        // 查找当前节点
        Optional<TreeNode> currentNode = allNodes.stream()
                .filter(node -> nodeId.equals(node.getIdentity()))
                .findFirst();

        if (currentNode.isPresent()) {
            String parentId = currentNode.get().getParent();
            // 终止条件1：父节点为空（根节点），停止递归
            if (parentId == null || parentId.isBlank()) {
                return;
            }

            // 终止条件2：父节点等于folder参数，添加该父节点后停止递归
            if (folderParam != null && !folderParam.isBlank() && folderParam.equals(parentId)) {
                relatedIds.add(parentId);
                return;
            }

            // 非终止条件：添加父节点ID，继续递归
            relatedIds.add(parentId);
            collectParentIdsWithTermination(parentId, allNodes, relatedIds, folderParam);
        }
    }

    /**
     * 步骤6实现：构建树结构
     */
    private List<TreeNode> buildTree(List<TreeNode> nodes) {
        // 1. 构建节点ID到节点的映射
        Map<String, TreeNode> nodeMap = nodes.stream()
                .collect(Collectors.toMap(TreeNode::getIdentity, node -> {
                    // 初始化子节点列表
                    node.setChildren(new ArrayList<>());
                    return node;
                }));

        // 2. 组装父子关系
        List<TreeNode> rootNodes = new ArrayList<>();
        for (TreeNode node : nodes) {
            String parentId = node.getParent();
            if (parentId == null || parentId.isBlank()) {
                // 根节点
                rootNodes.add(node);
            } else {
                // 非根节点，添加到父节点的子列表
                TreeNode parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    parentNode.getChildren().add(node);
                }
            }
        }

        // 3. 排序（可选：按名称升序）
        sortTree(rootNodes);
        return rootNodes;
    }

    /**
     * 递归排序树节点（按名称升序）
     */
    private void sortTree(List<TreeNode> nodes) {
        // 排序当前层级
        nodes.sort(Comparator.comparing(TreeNode::getName, Comparator.nullsLast(String::compareTo)));
        // 递归排序子节点
        nodes.forEach(node -> sortTree(node.getChildren()));
    }
}

