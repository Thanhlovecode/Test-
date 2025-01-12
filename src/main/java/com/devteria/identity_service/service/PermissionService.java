package com.devteria.identity_service.service;

import com.devteria.identity_service.dto.response.PermissionResponse;
import com.devteria.identity_service.dto.request.PermissionRequest;

import java.util.List;

public interface PermissionService {
    void createPermission(PermissionRequest request);
    List<PermissionResponse> getALlPermission();
    void deletePermission(String permission);
}
