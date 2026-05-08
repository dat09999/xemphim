package com.example.xemphim.DTO.Favor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class FavourSort {

    private Long tittleId;
    private String tittleName;

    private Long totalFavour;


}