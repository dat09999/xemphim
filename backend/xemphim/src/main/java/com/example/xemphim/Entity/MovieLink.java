package com.example.xemphim.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "movie_link")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String link;

    @OneToOne
    @JoinColumn(name = "tittle_id", referencedColumnName = "id", unique = true)
    private Tittle tittle;

    @Builder.Default
    private LocalDate Created=LocalDate.now();
}