package com.devteria.identity_service.service.impl;

import com.devteria.identity_service.dto.reponse.PermissionResponse;
import com.devteria.identity_service.dto.request.PermissionRequest;
import com.devteria.identity_service.entity.Permission;
import com.devteria.identity_service.repository.PermissionRepository;
import com.devteria.identity_service.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl  implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createPermission(PermissionRequest request) {
        Permission permission = modelMapper.map(request, Permission.class);
        permissionRepository.save(permission);
    }

    @Override
    public List<PermissionResponse> getALlPermission() {
       return permissionRepository.findAll()
               .stream()
               .map((element) -> modelMapper.map(element, PermissionResponse.class))
               .toList();
    }

    @Override
    public void deletePermission(String permission) {
        permissionRepository.deleteById(permission);
    }
}
