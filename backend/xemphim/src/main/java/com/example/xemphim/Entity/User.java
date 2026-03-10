package com.example.xemphim.Entity;

import com.example.xemphim.Enum.Gender;
import com.example.xemphim.Enum.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.example.xemphim.Enum.UserStatus;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "users") // tránh trùng keyword "user" trong một số DB
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;
    @NotBlank(message = "Mật khẩu không được trống")
    @Size(min = 8, max = 64, message = "Mật khẩu 8-64 ký tự")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String sdt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
    @Column(nullable = false)
    private Date birthday;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate = LocalDateTime.now();
}