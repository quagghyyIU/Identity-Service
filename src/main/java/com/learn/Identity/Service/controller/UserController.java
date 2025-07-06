package com.learn.Identity.Service.controller;

import com.learn.Identity.Service.Service.UserService;
import com.learn.Identity.Service.dto.request.ApiResponse;
import com.learn.Identity.Service.dto.request.UserCreationRequest;
import com.learn.Identity.Service.dto.request.UserUpdateRequest;
import com.learn.Identity.Service.dto.response.UserResponse;
import com.learn.Identity.Service.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    ApiResponse<User> createUser (@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createRequest(request));
        return apiResponse;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
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

}
