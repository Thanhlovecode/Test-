package com.devteria.identity_service.dto.response;


import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private String name;
    private String description;
    Set<PermissionResponse> permissions;
}
