package com.example.Ecom.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotNull(message = "username is required")
        @Size(min = 2, max = 100, message = "username must be at least 2 characters long")
        String username,

        @NotNull(message = "email is required")
        @Email(message = "Invalid email address")
        String email,

       @NotNull(message = "password is required")
       @Size(min = 2, max = 100, message = "password must be at least 2 characters long")
       String password,
        String token

) {
}
