package org.asura.ddd.structure.inventory.application.dto.query;

import org.asura.ddd.structure.common.dto.request.PageQueryRequest;

public class InventoryPageQuery extends PageQueryRequest {

    private String productId;

    public InventoryPageQuery() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}