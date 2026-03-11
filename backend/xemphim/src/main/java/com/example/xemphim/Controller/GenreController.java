package com.example.xemphim.Controller;

import com.example.xemphim.DTO.Genre.GenreRequest;
import com.example.xemphim.Entity.Genre;
import com.example.xemphim.Repository.GenreRepository;
import com.example.xemphim.Service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;
    private final GenreRepository genreRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenre(@PathVariable int id) {
        return ResponseEntity.ok(genreService.getGenre(id));
    }

    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }
    @GetMapping("/count")
    public ResponseEntity<String> countGenres() {
       long count = genreRepository.count();
       return ResponseEntity.ok(String.valueOf(count));
    }

    @PostMapping
    public ResponseEntity<String> addGenre(@RequestBody GenreRequest genreRequest) {
        genreService.addGenre(genreRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Add genre successfully");
    }
}