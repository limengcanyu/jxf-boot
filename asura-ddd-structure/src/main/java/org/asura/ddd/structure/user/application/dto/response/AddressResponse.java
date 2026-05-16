package org.asura.ddd.structure.user.application.dto.response;

import org.asura.ddd.structure.user.domain.model.valueobject.Address;

public class AddressResponse {

    private String province;
    private String city;
    private String district;
    private String detail;
    private String zipCode;

    public AddressResponse() {
    }

    public static AddressResponse from(Address address) {
        AddressResponse response = new AddressResponse();
        response.province = address.getProvince();
        response.city = address.getCity();
        response.district = address.getDistrict();
        response.detail = address.getDetail();
        response.zipCode = address.getZipCode();
        return response;
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