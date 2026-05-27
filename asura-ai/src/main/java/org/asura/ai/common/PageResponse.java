package org.asura.ai.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页响应类
 */
public class PageResponse<T> {

    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;

    /**
     * 默认无参构造函数，用于JSON序列化
     */
    public PageResponse() {
        this.content = new ArrayList<>();
    }

    /**
     * 带参数构造函数
     */
    public PageResponse(List<T> content, long totalElements, int pageNumber, int pageSize) {
        this.content = content != null ? content : new ArrayList<>();
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}