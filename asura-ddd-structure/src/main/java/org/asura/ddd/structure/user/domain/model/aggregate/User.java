package org.asura.ddd.structure.user.domain.model.aggregate;

import lombok.Getter;
import org.asura.ddd.structure.user.domain.model.valueobject.Address;
import org.asura.ddd.structure.user.domain.model.valueobject.PhoneNumber;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class User {

    private String id;
    private String username;
    private String email;
    private PhoneNumber phoneNumber;
    private Address address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean enabled;

    private User() {
    }

    public static User create(String username, String email, PhoneNumber phoneNumber, Address address) {
        User user = new User();
        user.id = UUID.randomUUID().toString();
        user.username = username;
        user.email = email;
        user.phoneNumber = phoneNumber;
        user.address = address;
        user.createdAt = LocalDateTime.now();
        user.updatedAt = LocalDateTime.now();
        user.enabled = true;
        return user;
    }

    public void updateProfile(String email, PhoneNumber phoneNumber, Address address) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }

    public void disable() {
        this.enabled = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void enable() {
        this.enabled = true;
        this.updatedAt = LocalDateTime.now();
    }

}