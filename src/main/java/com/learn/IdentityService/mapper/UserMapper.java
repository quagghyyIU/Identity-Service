package com.learn.IdentityService.mapper;

import com.learn.IdentityService.dto.request.UserCreationRequest;
import com.learn.IdentityService.dto.request.UserUpdateRequest;
import com.learn.IdentityService.dto.response.UserResponse;
import com.learn.IdentityService.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
