package com.devteria.identity_service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgetPasswordRequest {
    private String username;
}
