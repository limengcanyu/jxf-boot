package com.spring.boot.tree.doubao;

import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 阶段指南查询工具类（三条件任意组合查询，修复文件夹查询问题）
 */
public class GuidanceQueryTool {

    /**
     * 构建多根节点树（确保所有节点正确挂载）
     */
    public static List<GuidanceVO> buildMultiRootTree(List<GuidanceVO> nodes) {
        if (nodes == null) {
            return new ArrayList<>();
        }

        Map<String, GuidanceVO> nodeMap = nodes.stream()
                .collect(Collectors.toMap(
                        GuidanceVO::getIdentity,
                        node -> node,
                        (existing, replacement) -> existing
                ));

        List<GuidanceVO> roots = new ArrayList<>();
        for (GuidanceVO node : nodes) {
            if (node.getParent() == null) {
                roots.add(node);
            } else {
                GuidanceVO parentNode = nodeMap.get(node.getParent());
                if (parentNode != null) {
                    if (!parentNode.getChildrenList().contains(node)) {
                        parentNode.getChildrenList().add(node);
                    }
                } else {
                    // 父节点不存在时仍保留节点，避免丢失
                    roots.add(node);
                }
            }
        }
        return roots;
    }

    /**
     * 核心查询方法（支持folder、name、subType任意组合）
     */
    public static List<GuidanceVO> query(
            List<GuidanceVO> rootNodes,
            GuidanceDTO queryDTO) {

        if (rootNodes == null) rootNodes = new ArrayList<>();
        if (queryDTO == null) queryDTO = new GuidanceDTO();

        // 提取并预处理查询条件
        String folderId = queryDTO.getFolder();
        String nameQuery = queryDTO.getName();
        String subTypeQuery = queryDTO.getSubType();
        String processedNameQuery = (nameQuery == null) ? null : nameQuery.trim().toLowerCase();

        // 打印查询条件
        System.out.println("\n===== 查询条件 =====");
        System.out.println("folder: " + (folderId == null ? "未指定" : folderId));
        System.out.println("name: " + (nameQuery == null ? "未指定" : nameQuery));
        System.out.println("subType: " + (subTypeQuery == null ? "未指定" : subTypeQuery));

        // 场景1：指定文件夹查询（必须返回该文件夹，再结合其他条件过滤子节点）
        if (folderId != null && !folderId.trim().isEmpty()) {
            GuidanceVO targetNode = findNode(rootNodes, folderId);
            if (targetNode == null) {
                System.out.println("未找到ID为[" + folderId + "]的节点");
                return new ArrayList<>();
            }

            // 对目标文件夹的子节点应用过滤（目标文件夹本身必须保留）
            GuidanceVO filteredResult = copyNodeWithChildren(targetNode,
                    filterChildren(targetNode.getChildrenList(), processedNameQuery, subTypeQuery));
            return Collections.singletonList(filteredResult);
        }

        // 场景2：全局查询（无folder条件，按name和subType过滤）
        List<GuidanceVO> result = new ArrayList<>();
        for (GuidanceVO root : rootNodes) {
            GuidanceVO filteredSubtree = filterSubtree(root, processedNameQuery, subTypeQuery);
            if (filteredSubtree != null) {
                result.add(filteredSubtree);
            }
        }
        return result;
    }

    /**
     * 单独过滤子节点列表（用于指定folder时，确保目标文件夹本身保留）
     */
    private static List<GuidanceVO> filterChildren(
            List<GuidanceVO> children,
            String nameQuery,
            String subTypeQuery) {
        List<GuidanceVO> filtered = new ArrayList<>();
        for (GuidanceVO child : children) {
            GuidanceVO filteredChild = filterSubtree(child, nameQuery, subTypeQuery);
            if (filteredChild != null) {
                filtered.add(filteredChild);
            }
        }
        return filtered;
    }

    /**
     * 递归查找节点（确保能找到目标文件夹）
     */
    private static GuidanceVO findNode(List<GuidanceVO> roots, String targetId) {
        for (GuidanceVO root : roots) {
            GuidanceVO found = findNodeRecursive(root, targetId);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private static GuidanceVO findNodeRecursive(GuidanceVO currentNode, String targetId) {
        if (targetId.equals(currentNode.getIdentity())) {
            return currentNode;
        }
        for (GuidanceVO child : currentNode.getChildrenList()) {
            GuidanceVO found = findNodeRecursive(child, targetId);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * 递归过滤子树（核心逻辑：三条件组合处理）
     */
    private static GuidanceVO filterSubtree(
            GuidanceVO node,
            String nameQuery,
            String subTypeQuery) {

        // 先处理子节点
        List<GuidanceVO> filteredChildren = new ArrayList<>();
        for (GuidanceVO child : node.getChildrenList()) {
            GuidanceVO filteredChild = filterSubtree(child, nameQuery, subTypeQuery);
            if (filteredChild != null) {
                filteredChildren.add(filteredChild);
            }
        }

        // 1. 名称匹配判断（name为空时默认匹配）
        boolean matchesName = (nameQuery == null || nameQuery.isEmpty())
                ? true
                : node.getCombinedName().toLowerCase().contains(nameQuery);

        // 2. 子类型匹配判断（subType为空时默认匹配）
        boolean matchesSubType = (subTypeQuery == null || subTypeQuery.trim().isEmpty())
                ? true
                : (node.getSubType() != null && node.getSubType().equals(subTypeQuery));

        // 3. 处理文件夹节点
        if (node.isFolder()) {
            // 文件夹保留规则：
            // - 自身匹配条件，或包含匹配条件的子节点（保证层级完整）
            if (matchesName && matchesSubType) {
                return copyNodeWithChildren(node, filteredChildren);
            } else if (!filteredChildren.isEmpty()) {
                return copyNodeWithChildren(node, filteredChildren);
            }
            return null;
        }

        // 4. 处理非文件夹节点
        // 非文件夹节点必须同时匹配name和subType（为空时默认匹配）
        return (matchesName && matchesSubType)
                ? copyNodeWithChildren(node, filteredChildren)
                : null;
    }

    /**
     * 复制节点并设置子节点
     */
    private static GuidanceVO copyNodeWithChildren(GuidanceVO original, List<GuidanceVO> children) {
        GuidanceVO copy = new GuidanceVO();
        BeanUtils.copyProperties(original, copy);
        copy.setChildrenList(children);
        return copy;
    }

    /**
     * 打印树结构
     */
    public static void printTrees(List<GuidanceVO> rootNodes, GuidanceDTO queryDTO) {
        if (rootNodes == null || rootNodes.isEmpty()) {
            System.out.println("查询结果为空");
            return;
        }
        System.out.println("\n===== 查询结果 =====");
        for (int i = 0; i < rootNodes.size(); i++) {
            System.out.println("----- 树 " + (i + 1) + " -----");
            printTree(rootNodes.get(i), "", queryDTO);
        }
    }

    private static void printTree(GuidanceVO node, String prefix, GuidanceDTO queryDTO) {
        boolean matchName = (queryDTO.getName() == null || queryDTO.getName().isEmpty())
                ? true
                : node.getCombinedName().toLowerCase().contains(queryDTO.getName().toLowerCase());

        boolean matchSubType = (queryDTO.getSubType() == null || queryDTO.getSubType().isEmpty())
                ? true
                : (node.getSubType() != null && node.getSubType().equals(queryDTO.getSubType()));

        System.out.printf("%s+ ID: %s | 名称: %s | 类型: %s | 子节点数: %d | 匹配名称: %s | 匹配类型: %s%n",
                prefix,
                node.getIdentity(),
                node.getCombinedName(),
                node.getSubType(),
                node.getChildrenList().size(),
                matchName ? "是" : "否",
                matchSubType ? "是" : "否"
        );

        for (GuidanceVO child : node.getChildrenList()) {
            printTree(child, prefix + "  ", queryDTO);
        }
    }

    /**
     * 测试方法（覆盖所有条件组合，重点测试文件夹查询）
     */
    public static void main(String[] args) {
        // 测试数据
        List<GuidanceVO> nodes = Arrays.asList(
                // 根节点
                new GuidanceVO("root", "总目录", "【", null, "folder"),

                // 文件夹A及其子节点
                new GuidanceVO("a", "设计文件夹", "（", "root", "folder"),
                new GuidanceVO("a1", "UI设计规范", "《", "a", "practice"),
                new GuidanceVO("a2", "交互设计指南", "《", "a", "practice"),

                // 文件夹B及其子节点
                new GuidanceVO("b", "开发文件夹", "（", "root", "folder"),
                new GuidanceVO("b1", "Java开发规范", "《", "b", "practice"),
                new GuidanceVO("b2", "前端开发手册", "《", "b", "manual"),

                // 空文件夹C
                new GuidanceVO("c", "测试文件夹", "（", "root", "folder")
        );

        // 构建树
        List<GuidanceVO> rootNodes = buildMultiRootTree(nodes);
        System.out.println("===== 完整树结构 =====");
        printTrees(rootNodes, new GuidanceDTO());

        // 测试1：仅folder查询（folder=a）
        System.out.println("\n===== 测试1：仅folder查询（folder=a） =====");
        GuidanceDTO query1 = new GuidanceDTO();
        query1.setFolder("a");
        printTrees(query(rootNodes, query1), query1);
        // 预期：返回文件夹a及其所有子节点（a1、a2）

        // 测试2：folder+name组合（folder=b，name=开发）
        System.out.println("\n===== 测试2：folder+name组合 =====");
        GuidanceDTO query2 = new GuidanceDTO();
        query2.setFolder("b");
        query2.setName("开发");
        printTrees(query(rootNodes, query2), query2);
        // 预期：返回文件夹b及其子节点中名称含"开发"的b1、b2

        // 测试3：folder+subType组合（folder=root，subType=practice）
        System.out.println("\n===== 测试3：folder+subType组合 =====");
        GuidanceDTO query3 = new GuidanceDTO();
        query3.setFolder("root");
        query3.setSubType("practice");
        printTrees(query(rootNodes, query3), query3);
        // 预期：返回root文件夹及其子节点中subType=practice的节点（a1、a2、b1）

        // 测试4：仅subType查询（subType=folder）
        System.out.println("\n===== 测试4：仅subType查询（查询所有文件夹） =====");
        GuidanceDTO query4 = new GuidanceDTO();
        query4.setSubType("folder");
        printTrees(query(rootNodes, query4), query4);
        // 预期：返回所有文件夹节点（root、a、b、c）

        // 测试5：仅name查询（name=规范）
        System.out.println("\n===== 测试5：仅name查询 =====");
        GuidanceDTO query5 = new GuidanceDTO();
        query5.setName("规范");
        printTrees(query(rootNodes, query5), query5);
        // 预期：返回名称含"规范"的节点（a1、b1）及其完整路径
    }
}

