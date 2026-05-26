package org.asura.restful.service;

import org.asura.restful.dto.request.UserCreateRequest;
import org.asura.restful.dto.request.UserUpdateRequest;
import org.asura.restful.dto.response.PageResponse;
import org.asura.restful.dto.response.UserResponse;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);

    UserResponse getUserById(String id);

    PageResponse<UserResponse> listUsers(int page, int size, String sortBy, String sortDir);

    UserResponse updateUser(String id, UserUpdateRequest request);

    void deleteUser(String id);
}