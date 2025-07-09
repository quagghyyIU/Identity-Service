package com.learn.Identity.Service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.Identity.Service.Service.AuthenticationService;
import com.learn.Identity.Service.dto.request.ApiResponse;
import com.learn.Identity.Service.dto.request.AuthenticationRequest;
import com.learn.Identity.Service.dto.response.AuthenticationResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;
    
    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        boolean result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
            .result(AuthenticationResponse.builder()
                .isAuthenticated(result)
                .build())
            .build();
    }
    
}
