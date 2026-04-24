package com.spring.boot.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private ValidatorService validatorService;

    @Override
    public String save(Person person) {
        String result = validatorService.validate(person);
        if (StringUtils.hasLength(result)) {
            return result;
        }
        return "hello";
    }

}
