//package com.devteria.identity_service.config;
//
//import com.devteria.identity_service.entity.User;
//import com.devteria.identity_service.enums.Role;
//import com.devteria.identity_service.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//
//@Configuration
//@RequiredArgsConstructor
//@Slf4j
//public class ApplicationInitConfig {
//     private  final PasswordEncoder passwordEncoder ;
//    @Bean
//    ApplicationRunner applicationRunner (UserRepository userRepository) {
//        return args -> {
//            if(!userRepository.existsByUsernameIgnoreCase("admin")) {
//                User user = User.builder()
//                        .username("admin")
//                        .password(passwordEncoder.encode("admin"))
//                        .firstname("Hà")
//                        .lastname("Thanh")
//                        .role(Role.ADMIN.name())
//                        .build();
//                userRepository.save(user);
//                log.warn("admin logged in");
//            }
//        };
//    }
//}
