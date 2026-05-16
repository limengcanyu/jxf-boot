package org.asura.ddd.structure.order.application.dto.command;

public class ShippingAddressDTO {

    private String province;
    private String city;
    private String district;
    private String detail;
    private String zipCode;

    public ShippingAddressDTO() {
    }

    public ShippingAddressDTO(String province, String city, String district, String detail, String zipCode) {
        this.province = province;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.zipCode = zipCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}