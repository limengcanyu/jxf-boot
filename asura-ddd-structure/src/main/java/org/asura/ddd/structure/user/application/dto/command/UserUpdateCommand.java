package org.asura.ddd.structure.user.application.dto.command;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserUpdateCommand {
    private String userId;
    private String email;
    private String phoneNumber;
    private AddressDTO address;

    public UserUpdateCommand() {
    }

    public UserUpdateCommand(String userId, String email, String phoneNumber, AddressDTO address) {
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

}