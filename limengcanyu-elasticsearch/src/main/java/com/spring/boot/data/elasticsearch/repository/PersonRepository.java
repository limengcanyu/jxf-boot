package com.spring.boot.data.elasticsearch.repository;

import com.spring.boot.data.elasticsearch.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/05/17 23:10
 */
public interface PersonRepository extends Repository<Person, String> {
    /**
     * ?0: 参数索引，从0开始
     *
     * @param firstName
     * @param pageable
     * @return
     */
    Page<Person> findByFirstName(String firstName, Pageable pageable);

    Page<Person> findByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);

}
