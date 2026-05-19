package org.asura.tree.tongyi;

import java.util.ArrayList;
import java.util.List;

public class Example {
    public static void main(String[] args) {
        // 假设这是你的数据
        List<TreeNode> data = loadData();

        // 创建查询服务
        TreeQueryService service = new TreeQueryService(data);

        // 示例1: 只按name模糊匹配
        QueryCriteria criteria1 = QueryCriteria.of("安全", null, null);
        List<TreeNode> result1 = service.searchAndBuildTree(criteria1);

        // 示例2: 只按文件夹identity查询
        QueryCriteria criteria2 = QueryCriteria.of(null, "folder-001", null);
        List<TreeNode> result2 = service.searchAndBuildTree(criteria2);

        // 示例3: 只按subtype查询
        QueryCriteria criteria3 = QueryCriteria.of(null, null, "checklist");
        List<TreeNode> result3 = service.searchAndBuildTree(criteria3);

        // 示例4: 多个条件组合查询
        QueryCriteria criteria4 = QueryCriteria.of("培训", null, "training");
        List<TreeNode> result4 = service.searchAndBuildTree(criteria4);

        // 示例5: 无查询条件，返回完整树
        List<TreeNode> completeTree = service.searchAndBuildTree(null);
    }

    private static List<TreeNode> loadData() {
        // 这里填充你的实际数据
        return new ArrayList<>();
    }
}
