package com.example.xemphim.DTO.Tittle;

import com.example.xemphim.Entity.Genre;
import com.example.xemphim.Entity.People;
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
public class TittleRequest {
    private String name;
    private String Original_name;
    private String Description;
    private String Country;
    private int Duration;
    private String Language;
    private LocalDate Release_date;
    private Set<Genre> genre=new HashSet<>();
    private Set<People> people=new HashSet<>();
    private Boolean featured;
}
