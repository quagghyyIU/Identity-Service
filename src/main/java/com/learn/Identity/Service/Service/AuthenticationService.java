package com.learn.Identity.Service.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.learn.Identity.Service.repository.UserRepository;
import com.learn.Identity.Service.dto.request.AuthenticationRequest;
import com.learn.Identity.Service.exception.AppException;
import com.learn.Identity.Service.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;

    public boolean authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        
        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }
}
