package com.devteria.identity_service.dto.reponse;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private LocalDate dob;
    private Set<RoleResponse> roles;
}
