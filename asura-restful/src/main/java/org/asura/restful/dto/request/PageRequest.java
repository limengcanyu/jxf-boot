package org.asura.restful.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "分页请求")
public class PageRequest {

    @Schema(description = "页码", example = "1", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "每页大小", example = "10", defaultValue = "10")
    private Integer size = 10;

    @Schema(description = "排序字段", example = "createdAt")
    private String sortBy;

    @Schema(description = "排序方向", example = "desc", allowableValues = {"asc", "desc"})
    private String sortDir = "desc";

    public PageRequest() {}

    public PageRequest(Integer page, Integer size, String sortBy, String sortDir) {
        this.page = page != null ? page : 1;
        this.size = size != null ? size : 10;
        this.sortBy = sortBy;
        this.sortDir = sortDir != null ? sortDir : "desc";
    }

    public static PageRequestBuilder builder() {
        return new PageRequestBuilder();
    }

    public static class PageRequestBuilder {
        private Integer page = 1;
        private Integer size = 10;
        private String sortBy;
        private String sortDir = "desc";

        public PageRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }

        public PageRequestBuilder size(Integer size) {
            this.size = size;
            return this;
        }

        public PageRequestBuilder sortBy(String sortBy) {
            this.sortBy = sortBy;
            return this;
        }

        public PageRequestBuilder sortDir(String sortDir) {
            this.sortDir = sortDir;
            return this;
        }

        public PageRequest build() {
            return new PageRequest(page, size, sortBy, sortDir);
        }
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

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }
}