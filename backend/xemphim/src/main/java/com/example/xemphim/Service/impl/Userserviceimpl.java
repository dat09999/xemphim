package com.example.xemphim.Service.impl;

import com.example.xemphim.DTO.User.UserRequest;
import com.example.xemphim.DTO.User.Userreponse;
import com.example.xemphim.Entity.User;
import com.example.xemphim.Enum.Role;
import com.example.xemphim.Enum.UserStatus;
import com.example.xemphim.Repository.UserRepository;
import com.example.xemphim.Service.Userservice;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class Userserviceimpl  implements Userservice {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Userreponse add(UserRequest user) {
        User user1=User.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .sdt(user.getPhone())
                .birthday(user.getDateOfBirth())
                .createDate(LocalDateTime.now())
                .status(UserStatus.ACTIVE)
                .role(Role.USER)
                .gender(user.getGender())


                .build();
        userRepository.save(user1);
        return Userreponse.builder()
                .fullname(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();

    }


    @Override
    public Userreponse update(User user) {
        return null;
    }

    @Override
    public Userreponse delete(User user) {
        return null;
    }
}
