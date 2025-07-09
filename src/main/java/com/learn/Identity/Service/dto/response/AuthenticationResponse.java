package com.learn.Identity.Service.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude(Include.NON_NULL)
public class AuthenticationResponse {
    boolean isAuthenticated;
    String username;
    String email;
    String firstName;
    String lastName;
    LocalDate dob;
}
