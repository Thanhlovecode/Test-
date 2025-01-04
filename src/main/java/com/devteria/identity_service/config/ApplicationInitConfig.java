//package com.devteria.identity_service.config;
//
//import com.devteria.identity_service.constant.PredefinedRole;
//import com.devteria.identity_service.entity.User;
//import com.devteria.identity_service.repository.RoleRepository;
//import com.devteria.identity_service.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.HashSet;
//import com.devteria.identity_service.entity.Role;
//
//@Configuration
//@RequiredArgsConstructor
//@Slf4j
//public class ApplicationInitConfig {
//     private  final PasswordEncoder passwordEncoder ;
//
//     private static final String admin="admin";
//
//
//    @Bean
//    ApplicationRunner applicationRunner (UserRepository userRepository, RoleRepository roleRepository) {
//        return args -> {
//            if(!userRepository.existsByUsernameIgnoreCase(admin)) {
//              Role adminRole = roleRepository.save(Role.builder()
//                        .name(PredefinedRole.ADMIN_ROLE)
//                        .description("Admin role")
//                        .build());
//
//                var roles = new HashSet<Role>();
//                roles.add(adminRole);
//                User user = User.builder()
//                        .username(admin)
//                        .password(passwordEncoder.encode(admin))
//                        .firstname("Hà")
//                        .lastname("Thanh")
//                        .roles(roles)
//                        .build();
//                userRepository.save(user);
//                log.warn("admin logged in");
//            }
//        };
//    }
//}
