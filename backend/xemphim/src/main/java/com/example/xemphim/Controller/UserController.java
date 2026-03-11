package com.example.xemphim.Controller;

import com.example.xemphim.DTO.Auth.AuthResponse;
import com.example.xemphim.DTO.Auth.LoginRequest;
import com.example.xemphim.DTO.User.Userreponse;
import com.example.xemphim.Entity.User;
import com.example.xemphim.Repository.UserRepository;
import com.example.xemphim.security.Jwtservice;
import com.example.xemphim.Service.impl.Userserviceimpl;
import com.example.xemphim.DTO.User.UserRequest;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final Userserviceimpl userService;
   private final UserRepository userRepository;
    private final Jwtservice jwtService;
    private final AuthenticationManager authenticationManager;
    @PostMapping("/add")
    public ResponseEntity<Userreponse> create(@RequestBody UserRequest req) {
        log.info("Request to create user: {}", req);
        return ResponseEntity.ok(userService.add(req));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        // lấy role từ authorities
        String role = auth.getAuthorities().iterator().next().getAuthority(); // "ROLE_USER"

        assert role != null;
        String token = jwtService.generateToken(req.getEmail(), Map.of("role", role));
        log.info("{}dang nhap thanh cong", req.toString());
        return ResponseEntity.ok(new AuthResponse(token, "Bearer"));
    }
    @GetMapping("/me")
    public Userreponse me( Authentication auth) {
        String email = auth.getName();
        User a= userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Email not found"));
        log.info("laythong tin thanh cong");
        return Userreponse.builder()
                .email(email)
                .fullname(a.getFullName())
                .dateofbirth(a.getBirthday())
                .gender(a.getGender())
                .phone(a.getSdt())
                .build();
    }
    @GetMapping("/count")
    public ResponseEntity<String> count() {
        long count = userRepository.count();
        log.info("laythong tin thanh cong");
        return ResponseEntity.ok(Long.toString(count));


    }
}