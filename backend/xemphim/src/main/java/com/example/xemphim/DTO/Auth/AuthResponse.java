package com.example.xemphim.DTO.Auth;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
}