package com.devteria.identity_service.service;


import com.devteria.identity_service.dto.reponse.AuthenticationResponse;
import com.devteria.identity_service.dto.reponse.IntrospectResponse;
import com.devteria.identity_service.dto.request.AuthenticationRequest;
import com.devteria.identity_service.dto.request.IntrospectRequest;
import com.devteria.identity_service.dto.request.LogoutRequest;
import com.devteria.identity_service.dto.request.RefreshRequest;
public interface AuthenticateService {
    AuthenticationResponse authenticated(AuthenticationRequest authenticationRequest);
    IntrospectResponse introspect(IntrospectRequest request);
    void logout(LogoutRequest request);
    AuthenticationResponse refreshToken(RefreshRequest request);
}
