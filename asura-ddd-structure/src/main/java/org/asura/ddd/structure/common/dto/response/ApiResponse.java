package org.asura.ddd.structure.common.dto.response;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public class ApiResponse<T> {

    private Integer code;
    private String message;
    private T data;
    
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private Integer pages;

    public ApiResponse() {
    }

    public ApiResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    public static <T> ApiResponse<T> success(List<T> records, IPage<T> page) {
        ApiResponse<T> response = new ApiResponse<>(200, "success", (T) records);
        response.setTotal(page.getTotal());
        response.setPageNum((int) page.getCurrent());
        response.setPageSize((int) page.getSize());
        response.setPages((int) page.getPages());
        return response;
    }

    public static <T> ApiResponse<T> success(String message, List<T> records, IPage<T> page) {
        ApiResponse<T> response = new ApiResponse<>(200, message, (T) records);
        response.setTotal(page.getTotal());
        response.setPageNum((int) page.getCurrent());
        response.setPageSize((int) page.getSize());
        response.setPages((int) page.getPages());
        return response;
    }

    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message, null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
}