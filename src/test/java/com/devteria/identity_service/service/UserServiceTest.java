package com.devteria.identity_service.service;

import com.devteria.identity_service.converter.UserConverter;
import com.devteria.identity_service.dto.response.UserResponse;
import com.devteria.identity_service.dto.request.UserRequest;
import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.enums.ErrorCode;
import com.devteria.identity_service.exceptions.UserExistedException;
import com.devteria.identity_service.exceptions.UserNotFoundException;
import com.devteria.identity_service.repository.UserRepository;
import com.devteria.identity_service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;


    @Mock
    private UserConverter userConverter;

    private UserRequest userRequest;

    private User user;

    private UserResponse userResponse;

    @BeforeEach
    void initData() {
        userRequest = UserRequest.builder()
                .username("Thanh")
                .firstname("Ha")
                .lastname("Thanh")
                .build();

        user = User.builder()
                .username("Thanh")
                .firstname("Ha")
                .lastname("Thanh")
                .build();

        userResponse = UserResponse.builder()
                .username("Thanh")
                .firstname("Ha")
                .lastname("Thanh")
                .build();

    }

    @Test
    void testCreate_WhenUsernameAlreadyExists_ThrowsException(){
        Mockito.when(userRepository.existsByUsernameIgnoreCase(userRequest.getUsername()))
                .thenReturn(true);
        UserExistedException exception = assertThrows(UserExistedException.class,
                () -> userService.createUser(userRequest));
        assertEquals(ErrorCode.USER_EXISTED.getMessage(),exception.getMessage());
        verify(userRepository,never()).save(any(User.class));
    }

    @Test
    void testFindUser_WhenFindUserById_ReturnUserResponse(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userConverter.convertUserResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.findUserById(1L);
        assertEquals(response.getUsername(),userRequest.getUsername());
    }

    @Test
    void testFindUser_WhenUserIdNotFound_ThrowsException(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                ()->userService.findUserById(1L));
        assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(),exception.getMessage());
    }

    @Test
    void testFindALlUser_ReturnListOfUsers(){
        List<User> users= List.of(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userConverter.convertUserResponse(user)).thenReturn(userResponse);
        List<UserResponse> responseList = userService.findAllUser();
        assertEquals(1,responseList.size());
        assertEquals(responseList.getFirst().getUsername(),userRequest.getUsername());
    }



}
