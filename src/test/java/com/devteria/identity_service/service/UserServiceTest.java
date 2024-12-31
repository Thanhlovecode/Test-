package com.devteria.identity_service.service;

import com.devteria.identity_service.dto.request.UserRequest;
import com.devteria.identity_service.exceptions.UserExistedException;
import com.devteria.identity_service.repository.UserRepository;
import com.devteria.identity_service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private UserRequest userRequest;

    @BeforeEach
    void initData() {
        userRequest = UserRequest.builder()
                .username("Thanh")
                .firstname("Ha")
                .lastname("Thanh")
                .build();
    }

    @Test
    void testCreate_WhenUsernameAlreadyExists_ThrowsException(){
        Mockito.when(userRepository.existsByUsernameIgnoreCase(userRequest.getUsername()))
                .thenReturn(true);

        assertThrows(UserExistedException.class, () -> userService.createUser(userRequest));
    }
}
