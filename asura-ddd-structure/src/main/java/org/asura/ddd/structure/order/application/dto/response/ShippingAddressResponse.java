package org.asura.ddd.structure.order.application.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.asura.ddd.structure.order.domain.model.valueobject.ShippingAddress;

@Setter
@Getter
public class ShippingAddressResponse {

    private String province;
    private String city;
    private String district;
    private String detail;
    private String zipCode;

    public ShippingAddressResponse() {
    }

    public static ShippingAddressResponse from(ShippingAddress address) {
        ShippingAddressResponse response = new ShippingAddressResponse();
        response.province = address.getProvince();
        response.city = address.getCity();
        response.district = address.getDistrict();
        response.detail = address.getDetail();
        response.zipCode = address.getZipCode();
        return response;
    }

}