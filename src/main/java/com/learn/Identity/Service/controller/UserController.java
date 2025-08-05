package com.learn.Identity.Service.controller;

import com.learn.Identity.Service.Service.UserService;
import com.learn.Identity.Service.dto.request.ApiResponse;
import com.learn.Identity.Service.dto.request.UserCreationRequest;
import com.learn.Identity.Service.dto.request.UserLoginRequest;
import com.learn.Identity.Service.dto.request.UserUpdateRequest;
import com.learn.Identity.Service.dto.response.UserResponse;
import com.learn.Identity.Service.entity.User;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    
    UserService userService;

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
        return userService.updateUser(userID, request);
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
