package org.asura.ddd.structure.user.application.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.asura.ddd.structure.user.domain.model.valueobject.Address;

@Setter
@Getter
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

}