package com.example.xemphim.DTO.Tittle;

import com.example.xemphim.Entity.Genre;
import com.example.xemphim.Entity.People;
import com.example.xemphim.Enum.TittleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TittleUpDate {
    private Long id;
    private String name;
    private String Original_name;
    private String Description;
    private TittleType tittleType;
    private String Country;
    private Integer Duration;
    private String Language;
    private LocalDate Release_date;
    private Set<Long> genre=new HashSet<>();
    private Set<Long> people=new HashSet<>();
    private Boolean featured;
}
