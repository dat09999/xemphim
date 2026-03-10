package com.example.xemphim.DTO.people;

import com.example.xemphim.Entity.Tittle;
import com.example.xemphim.Enum.Gender;
import com.example.xemphim.Enum.Role_People;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
    @AllArgsConstructor
    public class PeopleResponse {
        private Long id;
        private String name;
        private LocalDate birthday;
        private Gender gender;
        private String description;
        private String country;
        private Role_People role;
        public int getAge() {
            if (birthday == null) {
                return 0;
            }
            return Period.between(birthday, LocalDate.now()).getYears();
        }


    }