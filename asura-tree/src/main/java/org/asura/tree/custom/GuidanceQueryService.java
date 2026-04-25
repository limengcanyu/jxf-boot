package org.asura.tree.custom;

import org.asura.tree.doubao.GuidanceDTO;
import org.asura.tree.doubao.GuidanceVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GuidanceQueryService {

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
    public static List<GuidanceVO> query(List<GuidanceVO> allNodes, GuidanceDTO queryDTO) {
        List<GuidanceVO> result = null;
        // 根据文件夹查询，如果指定文件夹，结果就是文件夹及其子节点，如果没有指定，结果就是所有节点
        result = filteredByFolder(allNodes, queryDTO);
        // 根据名称查询，如果指定名称，结果就是符合条件的节点，如果没有指定，结果就是所有节点

        // 根据类型查询，如果指定类型，结果就是该类型的节点，如果没有指定，结果就是所有类型的节点

        return result;
    }

    public static List<GuidanceVO> filteredByFolder(List<GuidanceVO> allNodes, GuidanceDTO queryDTO) {
        if (queryDTO.getFolder() == null || queryDTO.getFolder().trim().isEmpty()) {
            return allNodes;
        } else {
            GuidanceVO forderNode = allNodes.stream().filter(node -> queryDTO.getFolder().equals(node.getIdentity())).findAny().orElse(null);

            // 查找该文件夹下的所有节点

            return null;
        }
    }

    public static List<GuidanceVO> findSubNodes(List<GuidanceVO> allNodes, GuidanceDTO queryDTO) {
        return null;
    }

}
