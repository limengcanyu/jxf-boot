package org.asura.ddd.structure.user.application.dto.command;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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