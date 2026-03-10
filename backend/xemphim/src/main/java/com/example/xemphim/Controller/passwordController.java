package com.example.xemphim.Controller;

import com.example.xemphim.DTO.Auth.ChangePassword;
import com.example.xemphim.DTO.Auth.ForgotPasswordRequest;
import com.example.xemphim.DTO.Auth.ResetPasswordRequest;
import com.example.xemphim.Service.impl.Passwordservice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j

public class passwordController {

    private final Passwordservice service;


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgot(@RequestBody ForgotPasswordRequest req) {
        service.requestOtp(req.email());
        // luôn trả giống nhau
        log.info("Forgot password request received");
        return ResponseEntity.ok().body(new ApiMessage("Nếu email tồn tại, mã OTP đã được gửi."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> reset(@RequestBody ResetPasswordRequest req) {
        service.resetPassword(req.email(), req.otp(), req.newPassword());
        return ResponseEntity.ok().body(new ApiMessage("Đổi mật khẩu thành công."));
    }
    @PostMapping("/change-password")
    public ResponseEntity<?> ChangePassword(Authentication authentication,@RequestBody ChangePassword req) {
        service.ChangePassword(authentication.getName(),req.password(),req.NewPassword());
        return ResponseEntity.ok().body(new ApiMessage("Đổi mật khẩu thành công."));
    }


    public record ApiMessage(String message) {}
}