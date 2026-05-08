package com.example.xemphim.DTO.Favor;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class FavourRankingDTO {
    private Long tittleId;
    private String tittleName;
    private Long totalFavour;
}