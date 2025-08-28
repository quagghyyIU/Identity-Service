package com.learn.IdentityService.service;

import com.learn.IdentityService.dto.request.UserUpdateRequest;
import com.learn.IdentityService.dto.response.UserResponse;
import com.learn.IdentityService.enums.Role;
import com.learn.IdentityService.exception.AppException;
import com.learn.IdentityService.exception.ErrorCode;
import com.learn.IdentityService.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learn.IdentityService.dto.request.UserCreationRequest;
import com.learn.IdentityService.entity.User;
import com.learn.IdentityService.repository.UserRepository;

import java.util.HashSet;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)   
public class UserService {
    
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Map request to user entity
        User user = userMapper.toUser(request);
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

//        user.setRoles(roles);

        // Save user
        User saved = userRepository.save(user);

        // Return user response
        return userMapper.toUserResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method getUsers");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse updateUser(String userID, UserUpdateRequest request) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);
        // If the request contains a new password, hash it before saving
        if(request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(10)
                .encode(request.getPassword()));
        }
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userID) {
        userRepository.deleteById(userID);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        log.info("In method getUser by ID");
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Transactional  // Removed readOnly = true to fix stored procedure execution
    public UserResponse validateUser(String username, String password) {
        // Option 1: Using stored procedure (if you have it in database)
        User user = userRepository.validateUserStoredProcedure(username, password);

        // Check if user is null
        if(user == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }
}
