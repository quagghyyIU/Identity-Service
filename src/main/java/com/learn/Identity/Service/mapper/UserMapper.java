package com.learn.Identity.Service.mapper;

import com.learn.Identity.Service.dto.request.UserCreationRequest;
import com.learn.Identity.Service.dto.request.UserUpdateRequest;
import com.learn.Identity.Service.dto.response.UserResponse;
import com.learn.Identity.Service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import javax.swing.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
    User updateUser(@MappingTarget User user, UserUpdateRequest request);
}
