package com.devteria.identity_service.exceptions.ControllerAdvice;

import com.devteria.identity_service.enums.ErrorCode;
import com.devteria.identity_service.exceptions.*;
import com.devteria.identity_service.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class HandleException{

    private static final String MIN_ATTRIBUTE= "min";

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex,WebRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getDescription(false).replace("uri=",""))
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UserExistedException.class)
    public ResponseEntity<ErrorResponse> handleUserExistException(UserExistedException ex, WebRequest request) {
        ErrorCode errorCode = ErrorCode.USER_EXISTED;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(errorCode.getHttpStatus().value())
                .path(request.getDescription(false).replace("uri=",""))
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception, WebRequest request) {
        String message = (Objects.requireNonNull(exception.getFieldError()).getDefaultMessage());
        var attributes = exception.getBindingResult().getAllErrors()
                .stream()
                .findFirst()
                .map(objectError -> objectError.unwrap(ConstraintViolation.class))
                .map(violation -> violation.getConstraintDescriptor().getAttributes())
                .orElse(null);
        String resultMessage = (attributes != null) ?
                Objects.requireNonNull(message).replace("{" + MIN_ATTRIBUTE + "}",
                        String.valueOf(attributes.get(MIN_ATTRIBUTE)))
                : message;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(resultMessage)
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler(UserNotExistedException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotExistedException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UnAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthenticationException(UnAuthenticationException ex
            , WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getDescription(false).replace("uri=",""))
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthenticationException(TokenValidationException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex ){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .message(errorCode.getMessage())
                        .build());
    }
}