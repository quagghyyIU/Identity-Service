package com.learn.Identity.Service.Service;

import com.learn.Identity.Service.dto.request.UserUpdateRequest;
import com.learn.Identity.Service.dto.response.UserResponse;
import com.learn.Identity.Service.exception.AppException;
import com.learn.Identity.Service.exception.ErrorCode;
import com.learn.Identity.Service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learn.Identity.Service.dto.request.UserCreationRequest;
import com.learn.Identity.Service.entity.User;
import com.learn.Identity.Service.repository.UserRepository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)   
public class UserService {
    
    UserRepository userRepository;
    UserMapper userMapper;
    
    public UserResponse createRequest(UserCreationRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Map request to user
        User user = userMapper.toUser(request);

        // Hash the raw password before saving
        if(user.getPassword() != null) {
            user.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(10)
                .encode(user.getPassword()));
        }

        // Save user
        User saved = userRepository.save(user);

        // Return user response
        return userMapper.toUserResponse(saved);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
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

    public UserResponse getUser(String id) {
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
}
