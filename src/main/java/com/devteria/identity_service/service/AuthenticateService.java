package com.devteria.identity_service.service;


import com.devteria.identity_service.dto.request.*;
import com.devteria.identity_service.dto.response.AuthenticationResponse;
import com.devteria.identity_service.dto.response.IntrospectResponse;

public interface AuthenticateService {
    AuthenticationResponse authenticated(AuthenticationRequest authenticationRequest);
    IntrospectResponse introspect(IntrospectRequest request);
    void logout(LogoutRequest request);
    AuthenticationResponse refreshToken(RefreshRequest request);
    AuthenticationResponse forgotPassword(ForgetPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
}
