package com.devteria.identity_service.config;

import com.devteria.identity_service.dto.reponse.ErrorResponse;
import com.devteria.identity_service.enums.ErrorCode;
import com.devteria.identity_service.exceptions.TokenValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint  implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ErrorCode errorCode = authException.getCause() instanceof TokenValidationException
                ? ErrorCode.TOKEN_INVALID
                : ErrorCode.UNAUTHENTICATED;
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.builder()
                        .message(errorCode.getMessage())
                .build()));
        response.flushBuffer();
    }

}
