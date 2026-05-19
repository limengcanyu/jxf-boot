package org.asura.ddd.structure.user.application.dto.response;

import org.asura.ddd.structure.user.domain.model.aggregate.User;

import java.time.format.DateTimeFormatter;

public class UserResponse {

    private String id;
    private String username;
    private String email;
    private String phoneNumber;
    private AddressResponse address;
    private String createdAt;
    private String updatedAt;
    private Boolean enabled;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public UserResponse() {
    }

    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.id = user.getId();
        response.username = user.getUsername();
        response.email = user.getEmail();
        response.phoneNumber = user.getPhoneNumber() != null ? user.getPhoneNumber().toString() : null;
        response.address = user.getAddress() != null ? AddressResponse.from(user.getAddress()) : null;
        response.createdAt = user.getCreatedAt() != null ? user.getCreatedAt().format(FORMATTER) : null;
        response.updatedAt = user.getUpdatedAt() != null ? user.getUpdatedAt().format(FORMATTER) : null;
        response.enabled = user.getEnabled();
        return response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public AddressResponse getAddress() {
        return address;
    }

    public void setAddress(AddressResponse address) {
        this.address = address;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}