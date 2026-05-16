package org.asura.ddd.structure.user.domain.model.valueobject;

public class PhoneNumber {

    private String countryCode;
    private String number;

    private PhoneNumber() {
    }

    public static PhoneNumber of(String countryCode, String number) {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.countryCode = countryCode;
        phoneNumber.number = number;
        return phoneNumber;
    }

    public static PhoneNumber ofChina(String number) {
        return of("+86", number);
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return countryCode.equals(that.countryCode) && number.equals(that.number);
    }

    @Override
    public int hashCode() {
        int result = countryCode.hashCode();
        result = 31 * result + number.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return countryCode + number;
    }
}