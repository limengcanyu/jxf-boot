package org.asura.ddd.structure.order.domain.model.valueobject;

import lombok.Getter;

@Getter
public class ShippingAddress {

    private String province;
    private String city;
    private String district;
    private String detail;
    private String zipCode;

    private ShippingAddress() {
    }

    public static ShippingAddress create(String province, String city, String district, String detail, String zipCode) {
        ShippingAddress address = new ShippingAddress();
        address.province = province;
        address.city = city;
        address.district = district;
        address.detail = detail;
        address.zipCode = zipCode;
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShippingAddress that = (ShippingAddress) o;
        return province.equals(that.province) &&
                city.equals(that.city) &&
                district.equals(that.district) &&
                detail.equals(that.detail) &&
                zipCode.equals(that.zipCode);
    }

    @Override
    public int hashCode() {
        int result = province.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + district.hashCode();
        result = 31 * result + detail.hashCode();
        result = 31 * result + zipCode.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return province + city + district + detail + zipCode;
    }
}