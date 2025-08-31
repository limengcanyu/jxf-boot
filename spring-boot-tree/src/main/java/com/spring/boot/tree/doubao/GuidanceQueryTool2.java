package com.spring.boot.tree.doubao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 指南查询工具类（三条件任意组合查询，修复文件夹查询问题）
 * 先构建多根节点树形结构，然后根据查询条件进行过滤
 */
public class GuidanceQueryTool2 {

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
                roots.add(node); // 添加根节点
            } else {
                // 节点父级节点
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
    public static List<GuidanceVO> query(List<GuidanceVO> nodes, GuidanceDTO queryDTO) {
        if (nodes == null) {
            nodes = new ArrayList<>();
        }
        if (queryDTO == null) {
            queryDTO = new GuidanceDTO();
        }

        // 提取并预处理查询条件
        String folderQuery = queryDTO.getFolder();
        String nameQuery = (queryDTO.getName() == null) ? null : queryDTO.getName().trim().toLowerCase();
        String subTypeQuery = queryDTO.getSubType();

        // 打印查询条件
        System.out.println("\n===== 查询条件 =====");
        StringBuilder sb = new StringBuilder();
        sb.append("folder: " + (folderQuery == null ? "未指定" : folderQuery));
        sb.append(" name: " + (nameQuery == null ? "未指定" : nameQuery));
        sb.append(" subType: " + (subTypeQuery == null ? "未指定" : subTypeQuery));
        System.out.println(sb);

        List<GuidanceVO> result = new ArrayList<>();

        GuidanceVO queryFolderNode = null;
        // 根据文件夹查询
        if (folderQuery != null) {
            queryFolderNode = nodes.stream().filter(node -> Objects.equals(node.getIdentity(), folderQuery)).findAny().orElse(null);
        }

        if (queryFolderNode != null) {
            result.add(queryFolderNode);
        }

        // 根据名称和子类型查询
        List<GuidanceVO> queryNameNodes = null;
        if (nameQuery != null) {
            queryNameNodes = nodes.stream().filter(node -> node.getCombinedName().contains(nameQuery)).toList();
        }

        if (queryNameNodes != null) {
            result.addAll(queryNameNodes);
        }

        List<GuidanceVO> querySubTypeNodes = null;
        if (subTypeQuery != null) {
            querySubTypeNodes = nodes.stream().filter(node -> Objects.equals(node.getSubType(), subTypeQuery)).toList();
        }

        if (querySubTypeNodes != null) {
            result.addAll(querySubTypeNodes);
        }

        System.out.println("\n===== 查询节点 =====");
        for (GuidanceVO node : result) {
            System.out.println(node);
        }

        return null;
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
        boolean matchName = queryDTO.getName() == null || queryDTO.getName().isEmpty() || node.getCombinedName().toLowerCase().contains(queryDTO.getName().toLowerCase());

        boolean matchSubType = queryDTO.getSubType() == null || queryDTO.getSubType().isEmpty() || (node.getSubType() != null && node.getSubType().equals(queryDTO.getSubType()));

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
        List<GuidanceVO> nodes = GuidanceVOGenerate.generate();

        // 根据文件夹查询
        GuidanceDTO queryDTO = new GuidanceDTO();
        queryDTO.setFolder("folder01");
        query(nodes, queryDTO);

        // 根据名称查询
        queryDTO = new GuidanceDTO();
        queryDTO.setName("模版");
        query(nodes, queryDTO);

        // 根据类型查询
        queryDTO = new GuidanceDTO();
        queryDTO.setSubType("checklist");
        query(nodes, queryDTO);

        // 根据文件夹、名称查询
        queryDTO = new GuidanceDTO();
        queryDTO.setFolder("folder01");
        queryDTO.setName("模版");
        query(nodes, queryDTO);

        // 根据文件夹、类型查询
        queryDTO = new GuidanceDTO();
        queryDTO.setFolder("folder01");
        queryDTO.setSubType("checklist");
        query(nodes, queryDTO);

        // 根据名称、类型查询
        queryDTO = new GuidanceDTO();
        queryDTO.setName("模版");
        queryDTO.setSubType("checklist");
        query(nodes, queryDTO);

        // 根据文件夹、名称、类型查询
        queryDTO = new GuidanceDTO();
        queryDTO.setFolder("folder01");
        queryDTO.setName("模版");
        queryDTO.setSubType("checklist");
        query(nodes, queryDTO);

    }
}

