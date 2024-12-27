package com.devteria.identity_service.service;

import com.devteria.identity_service.dto.reponse.RoleResponse;
import com.devteria.identity_service.dto.request.RoleRequest;

import java.util.List;

public interface RoleService {
    void createRole(RoleRequest request);
    List<RoleResponse> getAllRole();
    void deleteRole(String role);
}
