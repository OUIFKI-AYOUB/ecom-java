package com.example.Ecom.dtos;

public record ResetPasswordRequest(
        String token,
        String newPassword
) {
}
