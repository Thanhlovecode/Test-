package com.devteria.identity_service.repository;

import com.devteria.identity_service.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private User user;
    @BeforeEach
    void initData(){
        user = User.builder()
                .username("Thanh")
                .id(1L)
                .firstname("Ha")
                .lastname("Thanh")
                .password("123456")
                .dob(LocalDate.of(1990,1,1))
                .build();
        userRepository.save(user);
    }

    @Test
    void TestFindUsername_ReturnTrue(){
        Optional<User> userEntity = userRepository.findByUsername(user.getUsername());
        assertTrue("User not found",userEntity.isPresent());
    }

}
