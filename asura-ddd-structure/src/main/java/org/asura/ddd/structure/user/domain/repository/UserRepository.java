package org.asura.ddd.structure.user.domain.repository;

import org.asura.ddd.structure.user.domain.model.aggregate.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    void deleteById(String id);
}