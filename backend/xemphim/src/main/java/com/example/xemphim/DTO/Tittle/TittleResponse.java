package com.example.xemphim.DTO.Tittle;

import com.example.xemphim.DTO.people.PeopleResponse;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public record TittleResponse(
        Long id,
        String name,
        String originalName,
        String description,
        String country,
        Integer duration,
        LocalDate releseDate,
        Boolean featured,
        String titleStatus,
        List<String> genres,
        List<PeopleResponse> people
) {
}