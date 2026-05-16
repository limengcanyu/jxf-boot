package org.asura.ddd.structure.user.domain.model.valueobject;

public class Address {

    private String province;
    private String city;
    private String district;
    private String detail;
    private String zipCode;

    private Address() {
    }

    public static Address create(String province, String city, String district, String detail, String zipCode) {
        Address address = new Address();
        address.province = province;
        address.city = city;
        address.district = district;
        address.detail = detail;
        address.zipCode = zipCode;
        return address;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getDetail() {
        return detail;
    }

    public String getZipCode() {
        return zipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return province.equals(address.province) &&
                city.equals(address.city) &&
                district.equals(address.district) &&
                detail.equals(address.detail) &&
                zipCode.equals(address.zipCode);
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