package org.asura.activiti.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * 采购申请DTO
 * 
 * @author Auto Generated
 * @version 1.0
 */
public class PurchaseRequestDTO {

    @NotBlank(message = "申请人不能为空")
    private String applicant;

    @NotBlank(message = "采购物品不能为空")
    private String itemName;

    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量至少为1")
    private Long quantity;

    @NotNull(message = "单价不能为空")
    @DecimalMin(value = "0.01", message = "单价必须大于0")
    private BigDecimal unitPrice;

    @NotNull(message = "总金额不能为空")
    @DecimalMin(value = "0.01", message = "总金额必须大于0")
    private BigDecimal totalAmount;

    @NotBlank(message = "供应商不能为空")
    private String supplier;

    @NotBlank(message = "紧急程度不能为空")
    private String urgency;

    @NotBlank(message = "采购理由不能为空")
    private String reason;

    // 构造方法
    public PurchaseRequestDTO() {
    }

    public PurchaseRequestDTO(String applicant, String itemName, Long quantity, BigDecimal unitPrice, 
                             BigDecimal totalAmount, String supplier, String urgency, String reason) {
        this.applicant = applicant;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.supplier = supplier;
        this.urgency = urgency;
        this.reason = reason;
    }

    // Getter和Setter方法
    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "PurchaseRequestDTO{" +
                "applicant='" + applicant + '\'' +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalAmount=" + totalAmount +
                ", supplier='" + supplier + '\'' +
                ", urgency='" + urgency + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}