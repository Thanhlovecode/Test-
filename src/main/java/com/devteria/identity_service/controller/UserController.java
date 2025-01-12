package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.response.ResponseData;
import com.devteria.identity_service.dto.response.UserResponse;
import com.devteria.identity_service.dto.request.UserRequest;
import com.devteria.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
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

    @GetMapping("/advance-search-with-criteria")
    public ResponseEntity<ResponseData<?>> advanceSearchWithCriteria(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                       @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                       @RequestParam(required = false) String sortBy,
                                                       @RequestParam(defaultValue = "") String... search) {
        log.info("Request advance search query by criteria");
        ResponseData<?> responseData= ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data(userService.advanceSearchWithCriteria(pageNo,pageSize,sortBy,search))
                .build();
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/specification")
    public ResponseEntity<ResponseData<?>> advanceSearchWithSpecification(Pageable pageable,
                                                                          @ModelAttribute UserRequest userRequest){
       ResponseData<?> responseData = ResponseData.builder()
               .status(HttpStatus.OK.value())
               .message("Success")
               .data(userService.advanceSearchWithSpecification(pageable,userRequest))
               .build();
        return ResponseEntity.ok(responseData);
    }

}
