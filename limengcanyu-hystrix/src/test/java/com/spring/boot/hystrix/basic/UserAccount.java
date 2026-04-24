package com.spring.boot.hystrix.basic;

import lombok.Data;

@Data
public class UserAccount {
    private final int customerId;
    private final String name;
    private final String countryCode;
    private final boolean isFeatureXPermitted;
    private final boolean isFeatureYPermitted;
    private final boolean isFeatureZPermitted;

    UserAccount(int customerId, String name, String countryCode,
                boolean isFeatureXPermitted,
                boolean isFeatureYPermitted,
                boolean isFeatureZPermitted) {
        this.customerId = customerId;
        this.name = name;
        this.countryCode = countryCode;
        this.isFeatureXPermitted = isFeatureXPermitted;
        this.isFeatureYPermitted = isFeatureYPermitted;
        this.isFeatureZPermitted = isFeatureZPermitted;
    }
}
