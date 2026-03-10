package com.example.xemphim.Service.impl;

import com.example.xemphim.DTO.Tittle.TittleRequest;
import com.example.xemphim.DTO.Tittle.TittleResponse;
import com.example.xemphim.DTO.people.PeopleResponse;
import com.example.xemphim.Entity.Genre;
import com.example.xemphim.Entity.People;
import com.example.xemphim.Entity.Tittle;
import com.example.xemphim.Enum.TitleStatus;
import com.example.xemphim.Repository.PeopleRepository;
import com.example.xemphim.Repository.TittleRepository;
import com.example.xemphim.Service.TittleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TitleServiceimpl implements TittleService {

    private final TittleRepository tittleRepository;
    private final PeopleRepository peopleRepository;

    @Override
    public void add(TittleRequest tittle) {
        Tittle t = Tittle.builder()

                .country(tittle.getCountry())
                .name(tittle.getName())
                .Original_name(tittle.getOriginal_name())
                .description(tittle.getDescription())
                .Duration(tittle.getDuration())
                .genres(tittle.getGenre())
                .people(tittle.getPeople())
                .releseDate(tittle.getRelease_date())
                .featured(tittle.getFeatured())
                .build();

        tittleRepository.save(t);
    }

    @Override
    public TittleResponse findById(Long id) {
        Tittle tittle = tittleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No tittle found"));
        return toResponse(tittle);
    }

    @Override
    public List<TittleResponse> findAll() {
        return tittleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        Tittle a = tittleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No tittle found"));
        a.setTitleStatus(TitleStatus.NOT_PUBLISHED);
        tittleRepository.save(a);
    }

    @Override
    public List<TittleResponse> findByGenre(String genre) {
        if (genre == null || genre.isBlank()) return List.of();

        return tittleRepository.findByGenreNameIgnoreCase(genre.trim())
                .stream()
                .map(this::toResponse)
                .toList();
    }
    @Override
    public List<TittleResponse> findByPeople(Long Id) {
        People a=peopleRepository.getPeopleById(Id).orElseThrow(() -> new RuntimeException("No people found"));
        return tittleRepository.findByPeopleContaining(a).stream().map(this::toResponse).toList();


    }


    @Override
    public List<TittleResponse> findByCountry(String country) {
        return tittleRepository.findAllByCountry(country)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<TittleResponse> findByYear(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        return tittleRepository.findAllByReleseDateBetween(start, end)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<TittleResponse> getNewest3() {
        return tittleRepository.findAllByOrderByReleseDateDesc(PageRequest.of(0, 3))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<TittleResponse> getFeatured6() {
        List<Tittle> featured = tittleRepository
                .findAllByFeaturedTrueOrderByReleseDateDesc(PageRequest.of(0, 6));

        if (featured.isEmpty()) {
            return tittleRepository.findAllByOrderByReleseDateDesc(PageRequest.of(0, 6))
                    .stream()
                    .map(this::toResponse)
                    .toList();
        }

        return featured.stream()
                .map(this::toResponse)
                .toList();
    }

    private TittleResponse toResponse(Tittle tittle) {
        return new TittleResponse(
                tittle.getId(),
                tittle.getName(),
                tittle.getOriginal_name(),
                tittle.getDescription(),
                tittle.getCountry(),
                tittle.getDuration(),
                tittle.getReleseDate(),
                tittle.isFeatured(),
                tittle.getTitleStatus() != null ? tittle.getTitleStatus().name() : null,
                tittle.getGenres() != null
                        ? tittle.getGenres().stream().map(Genre::getName).toList()
                        : List.of(),
                tittle.getPeople() != null
                        ? tittle.getPeople().stream().map(this::convertPeople).toList()
                        : List.of()
        );
    }
    public PeopleResponse convertPeople(People a){
        return PeopleResponse.builder()
                .id(a.getId())
                .name(a.getName())
                .description(a.getDescription())
                .birthday(a.getBirthday())
                .gender(a.getGender())
                .country(a.getCountry())
                .role(a.getRole())
                .build();


    }
}