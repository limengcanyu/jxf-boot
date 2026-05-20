package org.asura.skywalking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    /**
     * 数据列表
     */
    private List<T> data;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 创建分页响应
     */
    public static <T> PageResponse<T> of(List<T> data, Integer pageNum, Integer pageSize, Long total) {
        int pages = (int) Math.ceil((double) total / pageSize);
        return PageResponse.<T>builder()
                .data(data)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .pages(pages)
                .build();
    }

}