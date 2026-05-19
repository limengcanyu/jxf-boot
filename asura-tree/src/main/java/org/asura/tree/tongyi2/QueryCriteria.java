package org.asura.tree.tongyi2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询条件类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class QueryCriteria {
    private String nameKeyword;        // name模糊匹配
    private String folderIdentity;     // 文件夹identity精确匹配
    private String subtype;            // subtype类型匹配

    public static QueryCriteria of(String nameKeyword, String folderIdentity, String subtype) {
        return new QueryCriteria(nameKeyword, folderIdentity, subtype);
    }

    public boolean hasAnyCriteria() {
        return (nameKeyword != null && !nameKeyword.trim().isEmpty()) ||
                (folderIdentity != null && !folderIdentity.trim().isEmpty()) ||
                (subtype != null && !subtype.trim().isEmpty());
    }
}
