package com.learn.Identity.Service.mapper;

import com.learn.Identity.Service.dto.request.UserCreationRequest;
import com.learn.Identity.Service.dto.request.UserUpdateRequest;
import com.learn.Identity.Service.dto.response.UserResponse;
import com.learn.Identity.Service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    @Mapping(target = "firstName", ignore = true)
    UserResponse toUserResponse(User user);
    User updateUser(@MappingTarget User user, UserUpdateRequest request);
}
