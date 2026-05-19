package org.asura.ddd.structure.order.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("t_order")
public class OrderEntity {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("shipping_address_province")
    private String shippingAddressProvince;

    @TableField("shipping_address_city")
    private String shippingAddressCity;

    @TableField("shipping_address_district")
    private String shippingAddressDistrict;

    @TableField("shipping_address_detail")
    private String shippingAddressDetail;

    @TableField("shipping_address_zip_code")
    private String shippingAddressZipCode;

    @TableField("total_amount")
    private BigDecimal totalAmount;

    @TableField("status")
    private String status;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @Version
    @TableField("version")
    private Integer version;

    public OrderEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShippingAddressProvince() {
        return shippingAddressProvince;
    }

    public void setShippingAddressProvince(String shippingAddressProvince) {
        this.shippingAddressProvince = shippingAddressProvince;
    }

    public String getShippingAddressCity() {
        return shippingAddressCity;
    }

    public void setShippingAddressCity(String shippingAddressCity) {
        this.shippingAddressCity = shippingAddressCity;
    }

    public String getShippingAddressDistrict() {
        return shippingAddressDistrict;
    }

    public void setShippingAddressDistrict(String shippingAddressDistrict) {
        this.shippingAddressDistrict = shippingAddressDistrict;
    }

    public String getShippingAddressDetail() {
        return shippingAddressDetail;
    }

    public void setShippingAddressDetail(String shippingAddressDetail) {
        this.shippingAddressDetail = shippingAddressDetail;
    }

    public String getShippingAddressZipCode() {
        return shippingAddressZipCode;
    }

    public void setShippingAddressZipCode(String shippingAddressZipCode) {
        this.shippingAddressZipCode = shippingAddressZipCode;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}