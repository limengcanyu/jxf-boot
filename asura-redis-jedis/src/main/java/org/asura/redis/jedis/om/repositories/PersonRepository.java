package org.asura.redis.jedis.om.repositories;

import com.redis.om.spring.repository.RedisDocumentRepository;
import org.asura.redis.jedis.om.domain.Person;

import java.util.List;

public interface PersonRepository extends RedisDocumentRepository<Person, String> {
    List<Person> findByLastNameAndFirstName(String lastName, String firstName);
}
