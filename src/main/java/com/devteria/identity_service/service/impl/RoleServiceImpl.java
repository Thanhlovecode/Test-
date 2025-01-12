package com.devteria.identity_service.service.impl;

import com.devteria.identity_service.dto.response.RoleResponse;
import com.devteria.identity_service.dto.request.RoleRequest;
import com.devteria.identity_service.entity.Role;
import com.devteria.identity_service.repository.PermissionRepository;
import com.devteria.identity_service.repository.RoleRepository;
import com.devteria.identity_service.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createRole(RoleRequest request) {
        Role role= modelMapper.map(request,Role.class);
        role.setPermissions(new HashSet<>(permissionRepository.findAllById(request.getPermissions())));
        roleRepository.save(role);
    }

    @Override
    public List<RoleResponse> getAllRole() {
        return roleRepository.findAll()
                .stream().map((element) -> modelMapper.map(element, RoleResponse.class))
                .toList();
    }

    @Override
    public void deleteRole(String role) {
        roleRepository.deleteById(role);
    }
}
