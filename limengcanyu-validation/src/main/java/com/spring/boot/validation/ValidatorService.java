package com.spring.boot.validation;

public interface ValidatorService {

    <T> String validate(T object, Class<?>... groups);

}
