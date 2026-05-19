package org.asura.ddd.structure.inventory.application.dto.command;

public class StockAdjustCommand {

    private String productId;
    private Integer quantity;

    public StockAdjustCommand() {
    }

    public StockAdjustCommand(String productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}