package com.example.xemphim.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavourDTO {
    private Long id;

    private Long tittleId;
    private String tittleName;
    private LocalDateTime createDate;
}