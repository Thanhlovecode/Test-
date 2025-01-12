package com.devteria.identity_service.service.impl;

import com.devteria.identity_service.constant.PredefinedRole;
import com.devteria.identity_service.dto.response.PageResponse;
import com.devteria.identity_service.entity.Role;

import com.devteria.identity_service.enums.ErrorCode;
import com.devteria.identity_service.exceptions.UserExistedException;
import com.devteria.identity_service.exceptions.UserNotFoundException;

import com.devteria.identity_service.converter.UserConverter;
import com.devteria.identity_service.dto.response.UserResponse;
import com.devteria.identity_service.dto.request.UserRequest;
import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.repository.RoleRepository;
import com.devteria.identity_service.repository.Specification.UserSpecification;
import com.devteria.identity_service.repository.UserRepository;
import com.devteria.identity_service.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public PageResponse<?> advanceSearchWithCriteria(int pageNo, int pageSize, String sortBy, String...search) {
        return userRepository.searchUserByCriteria(pageNo,pageSize,sortBy,search);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public PageResponse<?> advanceSearchWithSpecification(Pageable pageable,UserRequest userRequest) {
        Page<User> users = userRepository.findAll(UserSpecification.filter(userRequest),pageable);
        return PageResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElement(users.getTotalElements())
                .totalPage(users.getTotalPages())
                .items(users.stream().map(userConverter::convertUserResponse))
                .build();
    }

    @Override
    public void createUser(UserRequest userRequest) {
        User user = modelMapper.map(userRequest, User.class);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);
        try{
            userRepository.save(user);
        } catch(DataIntegrityViolationException exception){
            throw new UserExistedException(ErrorCode.USER_EXISTED.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UserResponse> findAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userConverter::convertUserResponse)
                .toList();
    }

    @PostAuthorize("returnObject.username==authentication.name")
    @Override
    public UserResponse findUserById(Long id) {
        return userConverter.convertUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found")));
    }

    @Override
    public void updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found to update"));
        modelMapper.map(userRequest, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAllById(userRequest.getRoles())));
        userRepository.save(user);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteUser(List<Long> ids) {
        userRepository.deleteAllById(ids);
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        return userConverter.convertUserResponse(userRepository
                .findByUsername(context.getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("User not found")));
    }
}
