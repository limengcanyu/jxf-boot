package org.asura.ddd.structure.order.application.dto.query;

import org.asura.ddd.structure.common.dto.request.QueryRequest;

public class OrderQuery extends QueryRequest {

    private String userId;
    private String status;

    public OrderQuery() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}