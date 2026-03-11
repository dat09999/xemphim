package com.example.xemphim.DTO.WatchHistory;

import com.example.xemphim.Entity.Tittle;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WatchHistoryReponse {
    private Long id;
    private String name;

    private LocalDateTime date;
}
