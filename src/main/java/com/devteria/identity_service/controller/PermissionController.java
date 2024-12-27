package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.reponse.PermissionResponse;
import com.devteria.identity_service.dto.request.PermissionRequest;
import com.devteria.identity_service.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/permission")
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<String> createPermission(@RequestBody PermissionRequest request){
        permissionService.createPermission(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Create successfully");
    }

    @GetMapping
    private List<PermissionResponse> getAllPermission(){
        return permissionService.getALlPermission();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deletePermission(@PathVariable("name") String name){
        permissionService.deletePermission(name);
        return ResponseEntity.ok("delete successfully");
    }
}
