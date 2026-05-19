package org.asura.ddd.structure.user.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.asura.ddd.structure.common.dto.response.ApiResponse;
import org.asura.ddd.structure.user.application.dto.command.AddressDTO;
import org.asura.ddd.structure.user.application.dto.command.UserRegisterCommand;
import org.asura.ddd.structure.user.application.dto.command.UserUpdateCommand;
import org.asura.ddd.structure.user.application.dto.query.UserPageQuery;
import org.asura.ddd.structure.user.application.dto.query.UserQuery;
import org.asura.ddd.structure.user.application.dto.response.UserResponse;
import org.asura.ddd.structure.user.domain.model.aggregate.User;
import org.asura.ddd.structure.user.domain.model.valueobject.Address;
import org.asura.ddd.structure.user.domain.model.valueobject.PhoneNumber;
import org.asura.ddd.structure.user.domain.repository.UserRepository;
import org.asura.ddd.structure.user.domain.service.UserDomainService;
import org.asura.ddd.structure.user.infrastructure.repository.UserRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserApplicationService {

    private final UserDomainService userDomainService;
    private final UserRepository userRepository;
    private final UserRepositoryImpl userRepositoryImpl;

    public UserApplicationService(UserDomainService userDomainService, 
                                  UserRepository userRepository,
                                  UserRepositoryImpl userRepositoryImpl) {
        this.userDomainService = userDomainService;
        this.userRepository = userRepository;
        this.userRepositoryImpl = userRepositoryImpl;
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

    public UserResponse updateProfile(String userId, UserUpdateCommand command) {
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
        User user = userDomainService.updateProfile(userId, command.getEmail(), phoneNumber, address);
        return UserResponse.from(user);
    }

    public void disable(String userId) {
        userDomainService.disableUser(userId);
    }

    public void enable(String userId) {
        userDomainService.enableUser(userId);
    }

    public void delete(String userId) {
        userRepository.deleteById(userId);
    }

    public ApiResponse<UserResponse> queryPage(UserPageQuery query) {
        IPage<User> page = userRepositoryImpl.findPage(
                query.getPageNum(), 
                query.getPageSize(), 
                query.getUsername(), 
                query.getEmail(), 
                query.getEnabled()
        );
        IPage<UserResponse> responsePage = page.convert(UserResponse::from);
        return ApiResponse.success(responsePage.getRecords(), responsePage);
    }

    public List<UserResponse> queryList(UserQuery query) {
        return userRepositoryImpl.findList(query.getUsername(), query.getEmail(), query.getEnabled())
                .stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }
}