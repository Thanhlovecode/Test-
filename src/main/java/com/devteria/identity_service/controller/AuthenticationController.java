package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.request.*;
import com.devteria.identity_service.dto.response.AuthenticationResponse;
import com.devteria.identity_service.dto.response.IntrospectResponse;
import com.devteria.identity_service.service.AuthenticateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticateService authenticateService;

    @PostMapping("/log-in")
    public AuthenticationResponse authenticationUser(@RequestBody AuthenticationRequest authenticationRequest){
        return authenticateService.authenticated(authenticationRequest);
    }

    @PostMapping("/log-out")
    public ResponseEntity<String> authenticationUser(@RequestBody LogoutRequest request){
        authenticateService.logout(request);
       return ResponseEntity.ok("Log out successfully");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest request){
        return ResponseEntity.ok(authenticateService.refreshToken(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<AuthenticationResponse> forgotPassword(@RequestBody ForgetPasswordRequest request){
        return ResponseEntity.ok(authenticateService.forgotPassword(request));
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request){
        authenticateService.resetPassword(request);
        return ResponseEntity.ok("Change password successfully");
    }

    @PostMapping("/introspect")
    public IntrospectResponse authenticationUser(@RequestBody IntrospectRequest request){
        return authenticateService.introspect(request);
    }



}
