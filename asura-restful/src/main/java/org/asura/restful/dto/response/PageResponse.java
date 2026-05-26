package org.asura.restful.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "分页响应")
public class PageResponse<T> {

    @Schema(description = "数据列表")
    private List<T> data;

    @Schema(description = "当前页码")
    private Integer page;

    @Schema(description = "每页大小")
    private Integer size;

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "总页数")
    private Integer pages;

    public PageResponse() {}

    public PageResponse(List<T> data, Integer page, Integer size, Long total, Integer pages) {
        this.data = data;
        this.page = page;
        this.size = size;
        this.total = total;
        this.pages = pages;
    }

    public static <T> PageResponse<T> builder() {
        return new PageResponse<>();
    }

    public PageResponse<T> data(List<T> data) {
        this.data = data;
        return this;
    }

    public PageResponse<T> page(Integer page) {
        this.page = page;
        return this;
    }

    public PageResponse<T> size(Integer size) {
        this.size = size;
        return this;
    }

    public PageResponse<T> total(Long total) {
        this.total = total;
        return this;
    }

    public PageResponse<T> pages(Integer pages) {
        this.pages = pages;
        return this;
    }

    public PageResponse<T> build() {
        return this;
    }

    public static <T> PageResponse<T> of(List<T> data, int page, int size, long total) {
        int pages = (int) Math.ceil((double) total / size);
        return new PageResponse<>(data, page, size, total, pages);
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
}