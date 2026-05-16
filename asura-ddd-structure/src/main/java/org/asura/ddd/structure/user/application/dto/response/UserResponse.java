package org.asura.ddd.structure.user.application.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.asura.ddd.structure.user.domain.model.aggregate.User;

import java.time.format.DateTimeFormatter;

@Setter
@Getter
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
        response.phoneNumber = user.getPhoneNumber() != null ? user.getPhoneNumber().getFullNumber() : null;
        response.address = user.getAddress() != null ? AddressResponse.from(user.getAddress()) : null;
        response.createdAt = user.getCreatedAt() != null ? user.getCreatedAt().format(FORMATTER) : null;
        response.updatedAt = user.getUpdatedAt() != null ? user.getUpdatedAt().format(FORMATTER) : null;
        response.enabled = user.getEnabled();
        return response;
    }

}