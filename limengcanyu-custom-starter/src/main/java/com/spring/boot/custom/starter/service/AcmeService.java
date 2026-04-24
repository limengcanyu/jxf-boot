package com.spring.boot.custom.starter.service;

import lombok.Data;

import java.time.Duration;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/01/22 15:44
 */
@Data
public class AcmeService {
    private boolean checkLocation;
    private Duration loginTimeout;

    public AcmeService(boolean checkLocation, Duration loginTimeout) {
        this.checkLocation = checkLocation;
        this.loginTimeout = loginTimeout;
    }

    public String print() {
        return this.toString();
    }
}
