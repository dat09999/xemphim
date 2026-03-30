package com.example.xemphim.Service;

import com.example.xemphim.DTO.Genre.GenreRequest;
import com.example.xemphim.Entity.Genre;

import java.util.List;

public interface GenreService {
    public Genre getGenre(Long id);

    public List<Genre> getAllGenres();
    public void addGenre(GenreRequest genre);
}
