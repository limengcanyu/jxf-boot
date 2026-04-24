package com.spring.boot.tree.tongyi2;


import java.util.ArrayList;
import java.util.List;

/**
 * 完整的测试类
 */
public class TreeQueryExample {

    public static void main(String[] args) {
        // 1. 准备测试数据
        List<TreeNode> allNodes = prepareTestData();

        // 2. 创建树结构服务
        TreeStructureService treeService = new TreeStructureService(allNodes);

        System.out.println("=== 树形结构查询演示 ===\n");

        // 3. 执行各种组合查询
        demonstrateAllCombinations(treeService);
    }

    /**
     * 准备测试数据
     */
    private static List<TreeNode> prepareTestData() {
        List<TreeNode> nodes = new ArrayList<>();

        // 根节点
        nodes.add(new TreeNode("root", "根目录", "folder", null));
        nodes.add(new TreeNode("dept-hr", "人力资源部", "department", "root"));
        nodes.add(new TreeNode("dept-it", "信息技术部", "department", "root"));
        nodes.add(new TreeNode("dept-finance", "财务部", "department", "root"));

        // 人力资源部子节点
        nodes.add(new TreeNode("hr-folder-001", "招聘管理", "folder", "dept-hr"));
        nodes.add(new TreeNode("hr-folder-002", "培训管理", "folder", "dept-hr"));
        nodes.add(new TreeNode("hr-doc-001", "年度招聘计划", "plan", "hr-folder-001"));
        nodes.add(new TreeNode("hr-doc-002", "安全培训方案", "training", "hr-folder-002"));
        nodes.add(new TreeNode("hr-doc-003", "员工手册", "document", "hr-folder-001"));

        // 信息技术部子节点
        nodes.add(new TreeNode("it-folder-001", "项目管理", "folder", "dept-it"));
        nodes.add(new TreeNode("it-folder-002", "安全管理", "folder", "dept-it"));
        nodes.add(new TreeNode("it-doc-001", "年度IT规划", "plan", "it-folder-001"));
        nodes.add(new TreeNode("it-doc-002", "网络安全方案", "security", "it-folder-002"));
        nodes.add(new TreeNode("it-doc-003", "系统维护记录", "report", "it-folder-001"));

        // 财务部子节点
        nodes.add(new TreeNode("finance-folder-001", "预算管理", "folder", "dept-finance"));
        nodes.add(new TreeNode("finance-folder-002", "报表管理", "folder", "dept-finance"));
        nodes.add(new TreeNode("finance-doc-001", "年度预算", "plan", "finance-folder-001"));
        nodes.add(new TreeNode("finance-doc-002", "财务分析报告", "report", "finance-folder-002"));
        nodes.add(new TreeNode("finance-doc-003", "安全审计", "audit", "finance-folder-002"));

        return nodes;
    }

    /**
     * 演示所有可能的查询组合
     */
    private static void demonstrateAllCombinations(TreeStructureService treeService) {
        // 场景1: 只按name查询
        System.out.println("1. 按名称'安全'查询:");
        QueryCriteria criteria1 = QueryCriteria.of("安全", null, null);
        List<TreeNode> result1 = treeService.queryInTree(criteria1);
        printResults(result1);

        // 场景2: 只按folderIdentity查询
        System.out.println("2. 按folderIdentity'hr-folder-002'查询:");
        QueryCriteria criteria2 = QueryCriteria.of(null, "hr-folder-002", null);
        List<TreeNode> result2 = treeService.queryInTree(criteria2);
        printResults(result2);

        // 场景3: 只按subtype查询
        System.out.println("3. 按subtype'plan'查询:");
        QueryCriteria criteria3 = QueryCriteria.of(null, null, "plan");
        List<TreeNode> result3 = treeService.queryInTree(criteria3);
        printResults(result3);

        // 场景4: name + folderIdentity组合
        System.out.println("4. 按名称'培训'和folderIdentity'hr-folder-002'查询:");
        QueryCriteria criteria4 = QueryCriteria.of("培训", "hr-folder-002", null);
        List<TreeNode> result4 = treeService.queryInTree(criteria4);
        printResults(result4);

        // 场景5: name + subtype组合
        System.out.println("5. 按名称'年度'和subtype'report'查询:");
        QueryCriteria criteria5 = QueryCriteria.of("年度", null, "report");
        List<TreeNode> result5 = treeService.queryInTree(criteria5);
        printResults(result5);

        // 场景6: folderIdentity + subtype组合
        System.out.println("6. 按folderIdentity'it-folder-002'和subtype'security'查询:");
        QueryCriteria criteria6 = QueryCriteria.of(null, "it-folder-002", "security");
        List<TreeNode> result6 = treeService.queryInTree(criteria6);
        printResults(result6);

        // 场景7: 三个条件全部使用
        System.out.println("7. 按名称'招聘'、folderIdentity'hr-folder-001'和subtype'plan'查询:");
        QueryCriteria criteria7 = QueryCriteria.of("招聘", "hr-folder-001", "plan");
        List<TreeNode> result7 = treeService.queryInTree(criteria7);
        printResults(result7);

        // 场景8: 无任何条件
        System.out.println("8. 无查询条件（返回完整树）:");
        List<TreeNode> result8 = treeService.queryInTree(null);
        printResults(result8);
    }

    /**
     * 打印查询结果
     */
    private static void printResults(List<TreeNode> results) {
        if (results.isEmpty()) {
            System.out.println("  无匹配结果\n");
            return;
        }

        printTree(results, 0);
        System.out.println();
    }

    /**
     * 递归打印树形结构
     */
    private static void printTree(List<TreeNode> nodes, int level) {
        String indent = "  ".repeat(level);
        for (TreeNode node : nodes) {
            System.out.println(indent + "├─ " + node.getName() +
                    " (" + node.getSubtype() + ")");
            if (!node.getChildren().isEmpty()) {
                printTree(node.getChildren(), level + 1);
            }
        }
    }
}
