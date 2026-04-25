package org.asura.validation;

public interface ValidatorService {

    <T> String validate(T object, Class<?>... groups);

}
