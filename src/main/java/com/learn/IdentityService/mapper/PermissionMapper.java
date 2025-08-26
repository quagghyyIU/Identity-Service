package com.learn.IdentityService.mapper;

import com.learn.IdentityService.dto.request.PermissionRequest;
import com.learn.IdentityService.dto.response.PermissionResponse;
import com.learn.IdentityService.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
