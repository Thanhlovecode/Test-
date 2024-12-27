package com.devteria.identity_service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RoleRequest {
    private String name;
    private String description;
    Set<String> permissions;
}
