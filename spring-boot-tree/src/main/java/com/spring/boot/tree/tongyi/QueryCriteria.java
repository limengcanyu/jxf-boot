package com.spring.boot.tree.tongyi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryCriteria {
    private String nameKeyword;        // name模糊匹配
    private String folderIdentity;     // 文件夹identity精确匹配
    private String subtype;            // subtype类型匹配

    // 构造方法的静态工厂方法，便于使用
    public static QueryCriteria of(String nameKeyword, String folderIdentity, String subtype) {
        return new QueryCriteria(nameKeyword, folderIdentity, subtype);
    }

    // 判断是否有任何查询条件
    public boolean hasAnyCriteria() {
        return nameKeyword != null && !nameKeyword.isEmpty() ||
                folderIdentity != null && !folderIdentity.isEmpty() ||
                subtype != null && !subtype.isEmpty();
    }
}
