package org.asura.validation;

import jakarta.validation.Valid;

public interface PersonService {

    String save(@Valid Person person);

}
