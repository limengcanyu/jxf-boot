package com.spring.boot.shiro.result;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Description: 结果对象</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2017/12/6 0006
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 6288374846131788743L;

    /**
     * 状态码: 0-成功；非0-失败
     */
    private Integer statusCode = 0;

    /**
     * 信息: 成功时为空，错误时才有信息
     */
    private String message = "";

    /**
     * 单个对象
     */
    private T record;

    /**
     * 多个对象
     */
    private List<T> recordList;

    public Result() {
    }

    public Result(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getRecord() {
        return record;
    }

    public void setRecord(T record) {
        this.record = record;
    }

    public List<T> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<T> recordList) {
        this.recordList = recordList;
    }

    @Override
    public String toString() {
        return "Result{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", record=" + record +
                ", recordList=" + recordList +
                '}';
    }
}
