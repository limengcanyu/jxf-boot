package org.asura.ddd.structure.order.application.dto.command;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}