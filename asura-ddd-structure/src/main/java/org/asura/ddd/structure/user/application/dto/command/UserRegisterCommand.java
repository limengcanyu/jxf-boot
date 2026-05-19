package org.asura.ddd.structure.user.application.dto.command;

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

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
}