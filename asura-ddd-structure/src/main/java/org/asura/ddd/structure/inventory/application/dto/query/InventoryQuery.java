package org.asura.ddd.structure.inventory.application.dto.query;

import org.asura.ddd.structure.common.dto.request.QueryRequest;

public class InventoryQuery extends QueryRequest {

    private String productId;

    public InventoryQuery() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}