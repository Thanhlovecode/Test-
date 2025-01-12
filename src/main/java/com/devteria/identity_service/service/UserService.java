package com.devteria.identity_service.service;

import com.devteria.identity_service.dto.response.PageResponse;
import com.devteria.identity_service.dto.response.UserResponse;
import com.devteria.identity_service.dto.request.UserRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    void createUser(UserRequest userRequest);
    List<UserResponse> findAllUser();
    UserResponse findUserById(Long id);
    void updateUser(Long id,UserRequest userRequest);
    void deleteUser(List<Long> ids);
    UserResponse getMyInfo();
    PageResponse<?> advanceSearchWithCriteria(int pageNo, int pageSize, String sortBy, String... search);

    PageResponse<?> advanceSearchWithSpecification(Pageable pageable,UserRequest userRequest);
}
