package com.spring.boot.data.elasticsearch;

import com.spring.boot.data.elasticsearch.entity.Person;
import com.spring.boot.data.elasticsearch.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/05/17 23:20
 */
@Slf4j
@SpringBootTest
public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Test
    public void findPersonByFirstName() {
        String firstName = "artanis";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Person> personPage = personService.findByFirstName(firstName, pageable);
        log.debug("personPage: {}", personPage);

        int total = personPage.getTotalPages();
        List<Person> personList = personPage.getContent();
        log.debug("total: {} personList: {}", total, personList);
    }

    @Test
    public void findByFirstNameAndLastName() {
        String firstName = "artanis";
        String lastName = "xxx";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Person> personPage = personService.findByFirstNameAndLastName(firstName, lastName, pageable);
        log.debug("personPage: {}", personPage);

        int total = personPage.getTotalPages();
        List<Person> personList = personPage.getContent();
        log.debug("total: {} personList: {}", total, personList);
    }

}
