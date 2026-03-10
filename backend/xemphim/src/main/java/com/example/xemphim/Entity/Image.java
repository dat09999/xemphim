package com.example.xemphim.Entity;

import com.example.xemphim.Enum.Img;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Image {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Img kind;
    @Column(nullable = false)
    private Long ownerID;
    private String path;
    @Column(nullable = false,updatable=false)
    private LocalDateTime created=LocalDateTime.now();

}
