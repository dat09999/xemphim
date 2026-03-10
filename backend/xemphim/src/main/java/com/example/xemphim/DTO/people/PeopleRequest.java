package com.example.xemphim.DTO.people;

import com.example.xemphim.Enum.Gender;
import com.example.xemphim.Enum.Role_People;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
public class PeopleRequest {
    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String description;
    private Role_People role;
    private String country;

}
