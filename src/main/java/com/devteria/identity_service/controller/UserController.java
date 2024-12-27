package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.reponse.UserResponse;
import com.devteria.identity_service.dto.request.UserRequest;
import com.devteria.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody @Valid UserRequest userRequest){
        userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    @GetMapping
    public List<UserResponse> findAllUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("username: {}", authentication.getName());
        log.info("Role: {}", authentication.getAuthorities());
        return userService.findAllUser();
    }

    @GetMapping("/{id}")
    public UserResponse findUserById(@PathVariable("id") Long id){
        return userService.findUserById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") Long id,
                                             @RequestBody UserRequest userRequest){
        userService.updateUser(id, userRequest);
        return ResponseEntity.ok(" User updated successfully");
    }

    @DeleteMapping("{ids}")
    public ResponseEntity<String> deleteUser(@PathVariable("ids") List<Long> ids){
        userService.deleteUser(ids);
        return ResponseEntity.ok("Users deleted successfully");
    }

    @GetMapping("/myInfo")
    public UserResponse getMyInformation(){
        return userService.getMyInfo();
    }
}
