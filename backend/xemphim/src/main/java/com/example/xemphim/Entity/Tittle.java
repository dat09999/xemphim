package com.example.xemphim.Entity;

import com.example.xemphim.Enum.TitleStatus;
import com.example.xemphim.Enum.TittleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Tittle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "original_name")
    private String originalName;
    @Column(columnDefinition = "TEXT")
    private  String description;
    @ManyToMany
    @JoinTable(
            name = "title_genre",
            joinColumns = @JoinColumn(name = "title_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();
    private LocalDate releseDate;
    private int duration;
    public String country;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    public TitleStatus titleStatus= TitleStatus.PUBLISHED;
    @Builder.Default
    private LocalDate CreateDate= LocalDate.now();
    @Builder.Default
    private boolean featured=false;
    @Builder.Default
    private int views=0;
    @Enumerated(EnumType.STRING)

    private TittleType titleType;
    @ManyToMany
    @JoinTable(
            name = "title_people",
            joinColumns = @JoinColumn(name = "title_id"),
            inverseJoinColumns = @JoinColumn(name = "people_id")
    )
    private Set<People> people = new HashSet<>();

}
