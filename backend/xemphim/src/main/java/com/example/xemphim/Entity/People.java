package com.example.xemphim.Entity;

import com.example.xemphim.Enum.Gender;
import com.example.xemphim.Enum.Role_People;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class People {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Role_People role;
    private String name;
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String Description;
    @Column(nullable = false)
    private String Country;
    @ManyToMany(mappedBy = "people")
    @JsonIgnore
    private Set<Tittle> titles=new HashSet<>();
}
