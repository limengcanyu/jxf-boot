package org.asura.ddd.structure.user.domain.service;

import org.asura.ddd.structure.user.domain.model.aggregate.User;
import org.asura.ddd.structure.user.domain.model.valueobject.Address;
import org.asura.ddd.structure.user.domain.model.valueobject.PhoneNumber;
import org.asura.ddd.structure.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserDomainService {

    private final UserRepository userRepository;

    public UserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String username, String email, PhoneNumber phoneNumber, Address address) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = User.create(username, email, phoneNumber, address);
        return userRepository.save(user);
    }

    public User updateProfile(String userId, String email, PhoneNumber phoneNumber, Address address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getEmail().equals(email) && userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already used by another account");
        }

        user.updateProfile(email, phoneNumber, address);
        return userRepository.save(user);
    }

    public void disableUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.disable();
        userRepository.save(user);
    }

    public void enableUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.enable();
        userRepository.save(user);
    }
}