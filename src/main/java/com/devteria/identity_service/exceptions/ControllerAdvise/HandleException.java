package com.devteria.identity_service.exceptions.ControllerAdvise;

import com.devteria.identity_service.enums.ErrorCode;
import com.devteria.identity_service.exceptions.*;
import com.devteria.identity_service.dto.reponse.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class HandleException {

    private static final String MIN_ATTRIBUTE= "min";

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .detailError("User Not Found: PLease provide other id !!!")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UserExistedException.class)
    public ResponseEntity<ErrorResponse> handleUserExistException(UserExistedException ex){
        ErrorCode errorCode = ErrorCode.USER_EXISTED;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(errorCode.getMessage())
                .detailError("Please rename userName")
                .build();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception){
        String message =  (Objects.requireNonNull(exception.getFieldError()).getDefaultMessage());
       var attributes=exception.getBindingResult().getAllErrors()
                .stream()
                .findFirst()
                .map(objectError -> objectError.unwrap(ConstraintViolation.class))
                .map(violation->violation.getConstraintDescriptor().getAttributes())
                .orElse(null);
        String resultMessage= (attributes!= null) ?
                Objects.requireNonNull(message).replace("{"+MIN_ATTRIBUTE+"}",
                        String.valueOf(attributes.get(MIN_ATTRIBUTE)))
                : message;
        ErrorResponse errorResponse= ErrorResponse.builder()
                .message(resultMessage)
                .detailError("Error!!!")
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler(UserNotExistedException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotExistedException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .detailError("Error!!!")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UnAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthenticationException(UnAuthenticationException ex){

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthenticationException(TokenValidationException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .detailError("The provide token is invalid")
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