package org.asura.skywalking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户查询请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserQueryRequest {

    /**
     * 用户名称（模糊查询）
     */
    private String username;

    /**
     * 邮箱（模糊查询）
     */
    private String email;

    /**
     * 页码，从1开始
     */
    @Builder.Default
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    @Builder.Default
    private Integer pageSize = 10;

}