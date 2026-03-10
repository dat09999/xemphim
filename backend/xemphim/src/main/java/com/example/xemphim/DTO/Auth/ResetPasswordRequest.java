package com.example.xemphim.DTO.Auth;

public record ResetPasswordRequest(String email, String otp, String newPassword) {}