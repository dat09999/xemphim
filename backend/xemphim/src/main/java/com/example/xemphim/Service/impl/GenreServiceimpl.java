package com.example.xemphim.Service.impl;

import com.example.xemphim.DTO.Genre.GenreRequest;
import com.example.xemphim.Entity.Genre;
import com.example.xemphim.Repository.GenreRepository;
import com.example.xemphim.Service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class GenreServiceimpl  implements GenreService {
    private final GenreRepository genreRepository;
    @Override
    public Genre getGenre(Long id) {
        return genreRepository.findById(id).orElseThrow(() -> new RuntimeException("Genre not found"));
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public void addGenre(GenreRequest genre) {
        Genre a=Genre.builder()
                .name(genre.getGenre())

                .build();
        genreRepository.save(a);


    }
}
