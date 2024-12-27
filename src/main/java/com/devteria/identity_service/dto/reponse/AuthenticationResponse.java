package com.devteria.identity_service.dto.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {
    private String logIN;
    private String token;
    private String refreshToken;
    private boolean authenticated;
}
