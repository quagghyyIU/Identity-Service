package com.learn.IdentityService.Service;

import com.learn.IdentityService.dto.request.RoleRequest;
import com.learn.IdentityService.dto.response.RoleResponse;
import com.learn.IdentityService.mapper.RoleMapper;
import com.learn.IdentityService.repository.PermissionRepository;
import com.learn.IdentityService.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    public RoleResponse create (RoleRequest request){
        var role = roleMapper.toRole(request);

        var permission = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permission));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> getAll(){
        return roleRepository.findAll()
        .stream()
        .map(roleMapper::toRoleResponse)
        .toList();
    }

    public void delete(String role){
        roleRepository.deleteById(role);
    }

}
