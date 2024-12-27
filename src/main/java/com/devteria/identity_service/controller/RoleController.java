package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.reponse.RoleResponse;
import com.devteria.identity_service.dto.request.RoleRequest;
import com.devteria.identity_service.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/role")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<String> createRole(@RequestBody RoleRequest request){
        roleService.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Create successfully");
    }

    @GetMapping
    private List<RoleResponse> getALlRole(){
        return roleService.getAllRole();
    }

    @DeleteMapping("/{role}")
    public ResponseEntity<String> deleteRole(@PathVariable("role") String role){
        roleService.deleteRole(role);
        return ResponseEntity.ok("delete successfully");
    }
}
