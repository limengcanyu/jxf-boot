package com.spring.boot.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ValidatorServiceImpl implements ValidatorService {

    @Autowired
    private Validator validator;

    @Override
    public <T> String validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolationSet = validator.validate(object);
        String result = constraintViolationSet.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
        return result;
    }

}
