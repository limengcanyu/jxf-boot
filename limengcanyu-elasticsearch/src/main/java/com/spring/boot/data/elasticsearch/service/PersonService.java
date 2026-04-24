package com.spring.boot.data.elasticsearch.service;

import com.spring.boot.data.elasticsearch.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/05/17 23:14
 */
public interface PersonService {
    Page<Person> findByFirstName(String firstName, Pageable pageable);

    Page<Person> findByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);
}
