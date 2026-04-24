package com.spring.boot.tomcat;

import com.spring.boot.tomcat.entities.TreeNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class TreeNodeTests {
    @Test
    public void test() {
        List<TreeNode> treeNodeList = getTreeNodeList();
        log.debug("所有节点: {}", treeNodeList);

        treeNodeList.forEach(treeNode -> {
            // 过滤出本节点以外的节点
            List<TreeNode> otherNodeList = treeNodeList.stream().filter(treeNode1 -> !(Objects.equals(treeNode1.getNodeId(), treeNode.getNodeId()) && Objects.equals(treeNode1.getParentId(), treeNode.getParentId()))).toList();

            List<TreeNode> allParentList = getAllParentList(treeNode, otherNodeList);
//            log.debug("当前节点: {} 父级节点: {}", treeNode, allParentList);
        });
    }

    /**
     * (节点ID，父级ID)
     * <p>
     * (001, null)
     * (002, 001 )            | (003, 001)            | (004, 001) | (005, 001)
     * (006, 002 ) (007, 002) | (008, 003) (009, 003) | (010, 004) | (011, 005)
     * (002, 006 ) (012, 007) | (013, 008) (003, 009) | (014, 010) | (015, 011)
     *
     * @return
     */
    public List<TreeNode> getAllParentList(TreeNode treeNode, List<TreeNode> otherNodeList) {
        List<TreeNode> parentNodeList = new ArrayList<>(); // 父级节点list
        List<TreeNode> cycleNodeList = new ArrayList<>(); // 循环节点list

        TreeNode currentNode = treeNode;
        while (!Objects.isNull(currentNode) && !Objects.isNull(currentNode.getParentId())) {
            // 过滤出父级节点
            TreeNode finalCurrentNode = currentNode;
            TreeNode parentNode = otherNodeList.stream().filter(otherNode -> Objects.equals(finalCurrentNode.getParentId(), otherNode.getNodeId())).findFirst().orElse(null);

            if (!Objects.isNull(parentNode)) {
                parentNodeList.add(parentNode);

                if (Objects.equals(treeNode.getNodeId(), parentNode.getNodeId())) {
                    cycleNodeList.add(parentNode);
                }

                currentNode = parentNode;
            } else {
                currentNode = null;
            }

        }

        log.debug("getAllParentList 当前节点: {} 父级节点: {}", treeNode, parentNodeList);
        log.debug("getAllParentList 当前节点: {} 循环节点: {}", treeNode, cycleNodeList);
        return parentNodeList;
    }

    /**
     * (节点ID，父级ID)
     * <p>
     * (001, null)
     * (002, 001 )            | (003, 001)            | (004, 001) | (005, 001)
     * (006, 002 ) (007, 002) | (008, 003) (009, 003) | (010, 004) | (011, 005)
     * (002, 006 ) (012, 007) | (013, 008) (003, 009) | (014, 010) | (015, 011)
     *
     * @return
     */
    public List<TreeNode> getTreeNodeList() {
        List<TreeNode> retList = new ArrayList<>();

        retList.add(TreeNode.builder().nodeId("001").parentId(null).build());

        retList.add(TreeNode.builder().nodeId("002").parentId("001").build());
        retList.add(TreeNode.builder().nodeId("003").parentId("001").build());
        retList.add(TreeNode.builder().nodeId("004").parentId("001").build());
        retList.add(TreeNode.builder().nodeId("005").parentId("001").build());

        retList.add(TreeNode.builder().nodeId("006").parentId("002").build());
        retList.add(TreeNode.builder().nodeId("007").parentId("002").build());
        retList.add(TreeNode.builder().nodeId("008").parentId("003").build());
        retList.add(TreeNode.builder().nodeId("009").parentId("003").build());
        retList.add(TreeNode.builder().nodeId("010").parentId("004").build());
        retList.add(TreeNode.builder().nodeId("011").parentId("005").build());

        retList.add(TreeNode.builder().nodeId("002").parentId("006").build());
        retList.add(TreeNode.builder().nodeId("012").parentId("007").build());
        retList.add(TreeNode.builder().nodeId("013").parentId("008").build());
        retList.add(TreeNode.builder().nodeId("003").parentId("009").build());
        retList.add(TreeNode.builder().nodeId("014").parentId("010").build());
        retList.add(TreeNode.builder().nodeId("015").parentId("011").build());

        return retList;
    }
}
