package com.learn.IdentityService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.learn.IdentityService.dto.request.RoleRequest;
import com.learn.IdentityService.dto.response.RoleResponse;
import com.learn.IdentityService.entity.Role;

@Mapper(componentModel = "spring", uses = PermissionMapper.class)
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}