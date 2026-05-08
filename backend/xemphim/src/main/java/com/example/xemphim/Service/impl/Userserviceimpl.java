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
import java.util.List;

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
    public void update(int id) {
        User a=userRepository.findById(id).orElseThrow(()->new RuntimeException("loi "));
        a.setStatus(UserStatus.ACTIVE);
        userRepository.save(a);

    }

    @Override
    public void delete(int id) {
        User a=userRepository.findById(id).orElseThrow(()->new RuntimeException("loi"));
        a.setStatus(UserStatus.INACTIVE);
        userRepository.save(a);

    }
    public Userreponse convertreponse(User user){
        return Userreponse.builder().role(user.getRole())
                .fullname(user.getFullName())
                .dateofbirth(user.getBirthday())
                .email(user.getEmail())
                .phone(user.getSdt())
                .gender(user.getGender())
                .id(Long.valueOf(user.getId()))
                .status(String.valueOf(user.getStatus()))
                .build();
    }

    @Override
    public List<Userreponse> getAllusers() {
        List<User>a=userRepository.findAll();
        return a.stream().map(this::convertreponse).toList();
    }
}
