package org.asura.ddd.structure.user.application.service;

import org.asura.ddd.structure.user.application.dto.command.AddressDTO;
import org.asura.ddd.structure.user.application.dto.command.UserRegisterCommand;
import org.asura.ddd.structure.user.application.dto.command.UserUpdateCommand;
import org.asura.ddd.structure.user.application.dto.response.UserResponse;
import org.asura.ddd.structure.user.domain.model.aggregate.User;
import org.asura.ddd.structure.user.domain.model.valueobject.Address;
import org.asura.ddd.structure.user.domain.model.valueobject.PhoneNumber;
import org.asura.ddd.structure.user.domain.repository.UserRepository;
import org.asura.ddd.structure.user.domain.service.UserDomainService;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationService {

    private final UserDomainService userDomainService;
    private final UserRepository userRepository;

    public UserApplicationService(UserDomainService userDomainService, UserRepository userRepository) {
        this.userDomainService = userDomainService;
        this.userRepository = userRepository;
    }

    public UserResponse register(UserRegisterCommand command) {
        Address address = Address.create(
                command.getAddress().getProvince(),
                command.getAddress().getCity(),
                command.getAddress().getDistrict(),
                command.getAddress().getDetail(),
                command.getAddress().getZipCode()
        );
        PhoneNumber phoneNumber = PhoneNumber.ofChina(command.getPhoneNumber());
        User user = userDomainService.register(
                command.getUsername(),
                command.getEmail(),
                phoneNumber,
                address
        );
        return UserResponse.from(user);
    }

    public UserResponse getById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserResponse.from(user);
    }

    public UserResponse getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserResponse.from(user);
    }

    public UserResponse updateProfile(UserUpdateCommand command) {
        Address address = null;
        if (command.getAddress() != null) {
            AddressDTO addressDTO = command.getAddress();
            address = Address.create(
                    addressDTO.getProvince(),
                    addressDTO.getCity(),
                    addressDTO.getDistrict(),
                    addressDTO.getDetail(),
                    addressDTO.getZipCode()
            );
        }
        PhoneNumber phoneNumber = command.getPhoneNumber() != null ?
                PhoneNumber.ofChina(command.getPhoneNumber()) : null;
        User user = userDomainService.updateProfile(command.getUserId(), command.getEmail(), phoneNumber, address);
        return UserResponse.from(user);
    }

    public void disable(String userId) {
        userDomainService.disableUser(userId);
    }

    public void enable(String userId) {
        userDomainService.enableUser(userId);
    }
}