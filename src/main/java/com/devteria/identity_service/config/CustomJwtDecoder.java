package com.devteria.identity_service.config;

import javax.crypto.spec.SecretKeySpec;

import com.devteria.identity_service.dto.request.IntrospectRequest;
import com.devteria.identity_service.enums.ErrorCode;
import com.devteria.identity_service.exceptions.UnAuthenticationException;
import com.devteria.identity_service.service.AuthenticateService;
import lombok.RequiredArgsConstructor;

import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {

    @NonFinal
    @Value("${SIGNER_KEY}")
    protected String signerKey;
    private final AuthenticateService authenticationService;

    @Override
    public Jwt decode(String token) throws JwtException {
        var response = authenticationService.introspect(
                IntrospectRequest.builder().token(token).build());

        if (!response.isValid()) throw new AuthenticationException("",
                new UnAuthenticationException(ErrorCode.UNAUTHENTICATED.getMessage())) {};

        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
        return nimbusJwtDecoder.decode(token);
    }
}