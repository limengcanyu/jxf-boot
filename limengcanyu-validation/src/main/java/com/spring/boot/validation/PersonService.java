package com.spring.boot.validation;

import jakarta.validation.Valid;

public interface PersonService {

    String save(@Valid Person person);

}
