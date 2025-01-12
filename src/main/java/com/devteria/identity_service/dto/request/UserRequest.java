package com.devteria.identity_service.dto.request;

import com.devteria.identity_service.validator.BirthConstraint;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @Size(min = 3,message = "Username must be at least {min} characters")
    private String username;

    @Size(min=5,message = "Password must be at least {min} characters")
    @Pattern(regexp="^\\S+$",message = "Password must not contain spaces")
    private String password;
    private String firstname;
    private String lastname;


    @BirthConstraint(min=18,message = "your age must be at least {min} ages")
    private LocalDate dob;

    private List<String> roles;



}
