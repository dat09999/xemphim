package com.example.xemphim.DTO.User;

import com.example.xemphim.Enum.Gender;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder

public class Userreponse {
        private String fullname;
        private String email;
        private String phone;
        private Gender gender;
        private Date dateofbirth;
        
}
