package com.example.xemphim.DTO.Tittle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor

@RequiredArgsConstructor
public class AddMovieResult {
    private Long movieId;
    private String message;
    private List<String> uploaded;
    private List<String> failed;

    public AddMovieResult(Long id, String s, List<String> uploaded, List<String> missing, List<String> failed) {
    }
}