package com.learn.IdentityService.controller;

import com.learn.IdentityService.Service.UserService;
import com.learn.IdentityService.dto.request.ApiResponse;
import com.learn.IdentityService.dto.request.UserCreationRequest;
import com.learn.IdentityService.dto.request.UserLoginRequest;
import com.learn.IdentityService.dto.request.UserUpdateRequest;
import com.learn.IdentityService.dto.response.UserResponse;
import com.learn.IdentityService.entity.User;
import com.learn.IdentityService.exception.AppException;
import com.learn.IdentityService.exception.ErrorCode;
import com.learn.IdentityService.mapper.UserMapper;
import com.learn.IdentityService.repository.RoleRepository;
import com.learn.IdentityService.repository.UserRepository;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    
    @PostMapping
    ApiResponse<UserResponse> createUser (@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("User {} is accessing the user list", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));;

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userID}")
    UserResponse getUser(@PathVariable("userID") String userID) {
        return userService.getUser(userID);
    }

    @PutMapping("/{userID}")
    UserResponse updateUser(@PathVariable String userID, @RequestBody UserUpdateRequest request) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
            userMapper.updateUser(user, request);
            
            // Only encode password if it's provided in the request
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            var roles = roleRepository.findAllById(request.getRoles());
            user.setRoles(new HashSet<>(roles));

            return userMapper.toUserResponse(userRepository.save(user));
    }

    @DeleteMapping("/{userID}")
    String deleteUser(@PathVariable String userID) {
        userService.deleteUser(userID);
        return "user has been deleted successfully";
    }

    @PostMapping("/login")
    UserResponse login(@RequestBody @Valid UserLoginRequest request) {
        return userService.validateUser(request.getUsername(), request.getPassword());
    }

    @GetMapping("/myinfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }
}
