package com.example.xemphim.DTO.User;

import com.example.xemphim.Enum.Gender;
import com.example.xemphim.Enum.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder

public class Userreponse {
        private Long id;
        private String fullname;
        private Role role;
        private String email;
        private String phone;
        private Gender gender;
        private Date dateofbirth;
        private String status;
        private LocalDateTime create;
        
}
