package com.devteria.identity_service.service;

import com.devteria.identity_service.dto.reponse.UserResponse;
import com.devteria.identity_service.dto.request.UserRequest;

import java.util.List;

public interface UserService {
    void createUser(UserRequest userRequest);
    List<UserResponse> findAllUser();
    UserResponse findUserById(Long id);
    void updateUser(Long id,UserRequest userRequest);
    void deleteUser(List<Long> ids);
    UserResponse getMyInfo();
}
