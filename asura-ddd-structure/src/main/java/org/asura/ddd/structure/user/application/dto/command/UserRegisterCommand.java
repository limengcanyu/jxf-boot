package org.asura.ddd.structure.user.application.dto.command;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRegisterCommand {

    private String username;
    private String email;
    private String phoneNumber;
    private AddressDTO address;

    public UserRegisterCommand() {
    }

    public UserRegisterCommand(String username, String email, String phoneNumber, AddressDTO address) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

}