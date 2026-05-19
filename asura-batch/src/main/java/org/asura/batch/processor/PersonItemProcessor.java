package org.asura.batch.processor;

import org.asura.batch.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * <p>Description: item转换接口</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2019/2/22 0022
 */
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    @Override
    public Person process(Person person) {
        if (person == null) {
            return null;
        }

        final String firstName = person.getFirstName() != null ? person.getFirstName().toUpperCase() : null;
        final String lastName = person.getLastName() != null ? person.getLastName().toUpperCase() : null;

        final Person transformedPerson = new Person(firstName, lastName);

        log.info("Converting (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }
}