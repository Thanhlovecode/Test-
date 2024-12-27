package com.devteria.identity_service.dto.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PermissionRequest {
    private String name;
    private String description;
}
