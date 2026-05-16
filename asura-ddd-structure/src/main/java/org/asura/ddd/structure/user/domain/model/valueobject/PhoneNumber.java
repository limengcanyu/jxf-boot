package org.asura.ddd.structure.user.domain.model.valueobject;

import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class PhoneNumber {

    private static final Pattern CHINA_PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    private final String countryCode;
    private final String number;

    private PhoneNumber(String countryCode, String number) {
        this.countryCode = countryCode;
        this.number = number;
    }

    public static PhoneNumber of(String countryCode, String number) {
        validate(countryCode, number);
        return new PhoneNumber(countryCode, number);
    }

    public static PhoneNumber ofChina(String number) {
        return of("+86", number);
    }

    private static void validate(String countryCode, String number) {
        if (countryCode == null || countryCode.isEmpty()) {
            throw new IllegalArgumentException("Country code cannot be empty");
        }
        if (number == null || number.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        if ("+86".equals(countryCode) && !CHINA_PHONE_PATTERN.matcher(number).matches()) {
            throw new IllegalArgumentException("Invalid China phone number format");
        }
    }

    public String getFullNumber() {
        return countryCode + number;
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
        return getFullNumber();
    }
}