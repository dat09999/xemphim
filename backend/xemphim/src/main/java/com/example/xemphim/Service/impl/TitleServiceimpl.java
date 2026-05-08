package com.example.xemphim.Service.impl;

import com.example.xemphim.DTO.Tittle.*;
import com.example.xemphim.DTO.people.PeopleResponse;
import com.example.xemphim.Entity.Genre;
import com.example.xemphim.Entity.People;
import com.example.xemphim.Entity.Tittle;
import com.example.xemphim.Enum.TitleStatus;
import com.example.xemphim.Enum.TittleType;
import com.example.xemphim.Repository.GenreRepository;
import com.example.xemphim.Repository.PeopleRepository;
import com.example.xemphim.Repository.TittleRepository;
import com.example.xemphim.Repository.WatchHistoryRepository;
import com.example.xemphim.Service.TittleService;
import com.example.xemphim.Specification.TittleSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TitleServiceimpl implements TittleService {

    private final TittleRepository tittleRepository;
    private final PeopleRepository peopleRepository;
    private final GenreRepository genreRepository;
    private final ImageService imageService;
    private final WatchHistoryRepository watchHistoryRepository;

    @Override
    public TittleResponse add(TittleRequest tittle) {
        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(tittle.getGenre()));
        Set<People> people = new HashSet<>(peopleRepository.findAllById(tittle.getPeople()));

        Tittle t = Tittle.builder()
                .country(tittle.getCountry())
                .name(tittle.getName())
                .originalName(tittle.getOriginal_name())
                .description(tittle.getDescription())
                .duration(tittle.getDuration())
                .titleType(TittleType.valueOf(tittle.getTittleType()))
                .titleStatus(TitleStatus.valueOf(tittle.getType()))
                .genres(genres)
                .people(people)
                .releseDate(tittle.getRelease_date())
                .featured(Boolean.TRUE.equals(tittle.getFeatured()))
                .build();

        tittleRepository.save(t);
        return toResponse(t);
    }
   @Override
   public AddMovieResult addwithLink(TittleRequest tittle,
                                     MultipartFile video,
                                     MultipartFile poster,
                                     MultipartFile banner) {

        TittleResponse a = add(tittle);

        List<String> uploaded = new ArrayList<>();
        List<String> missing = new ArrayList<>();
        List<String> failed = new ArrayList<>();

        if (video != null && !video.isEmpty()) {
            try {
                imageService.uploadVideo(a.id(), video);
                uploaded.add("VIDEO");
            } catch (Exception e) {
                failed.add("VIDEO");
            }
        } else {
            missing.add("VIDEO");
        }

        if (poster != null && !poster.isEmpty()) {
            try {
                imageService.uploadimage(a.id(), poster, "POSTER");
                uploaded.add("POSTER");
            } catch (Exception e) {
                failed.add("POSTER");
            }
        } else {
            missing.add("POSTER");
        }

        if (banner != null && !banner.isEmpty()) {
            try {
                imageService.uploadimage(a.id(), banner, "BANNER");
                uploaded.add("BANNER");
            } catch (Exception e) {
                failed.add("BANNER");
            }
        } else {
            missing.add("BANNER");
        }

        Tittle movie = tittleRepository.findById(a.id())
                .orElseThrow(() -> new RuntimeException("No tittle found"));

        if (!missing.isEmpty() || !failed.isEmpty()) {
            movie.setTitleStatus(TitleStatus.PENDING);
        }

        tittleRepository.save(movie);

        return new AddMovieResult(
                a.id(),
                missing.isEmpty() && failed.isEmpty()
                        ? "Tạo phim thành công"
                        : "Tạo phim thành công nhưng còn thiếu hoặc lỗi media",
                uploaded,
                missing,
                failed
        );
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
    public List<TittleResponse> filter(TittleFilterRequest tittle) {
        Sort sort = Sort.unsorted();

        if (tittle.getSortBy() != null && !tittle.getSortBy().isBlank()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(tittle.getSortDir())
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;

            sort = Sort.by(direction, tittle.getSortBy());
        } else {
            sort = Sort.by(Sort.Direction.DESC, "views")
                    .and(Sort.by(Sort.Direction.DESC, "releseDate"));
        }

        return tittleRepository.findAll(TittleSpecification.filterPublished(tittle), sort)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<TittleResponse> filterAdmin(TittleFilterRequest tittle) {
        Sort sort = Sort.unsorted();

        if (tittle.getSortBy() != null && !tittle.getSortBy().isBlank()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(tittle.getSortDir())
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;

            sort = Sort.by(direction, tittle.getSortBy());
        } else {
            sort = Sort.by(Sort.Direction.DESC, "views")
                    .and(Sort.by(Sort.Direction.DESC, "releseDate"));
        }

        return tittleRepository.findAll(TittleSpecification.filterForAdmin(tittle), sort)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public TittleResponse update(TittleUpDate tittle) {
        if (tittle.getId() == null) {
            throw new RuntimeException("Id is required");
        }

        Tittle existing = tittleRepository.findById(tittle.getId())
                .orElseThrow(() -> new RuntimeException("No tittle found"));

        if (tittle.getName() != null && !tittle.getName().isBlank()) {
            existing.setName(tittle.getName());
        }
        if (tittle.getTittleType()!= null) {
            existing.setTitleType(tittle.getTittleType());
        }


        if (tittle.getOriginal_name() != null && !tittle.getOriginal_name().isBlank()) {
            existing.setOriginalName(tittle.getOriginal_name());
        }

        if (tittle.getDescription() != null && !tittle.getDescription().isBlank()) {
            existing.setDescription(tittle.getDescription());
        }

        if (tittle.getCountry() != null && !tittle.getCountry().isBlank()) {
            existing.setCountry(tittle.getCountry());
        }

        if (tittle.getDuration() != null) {
            existing.setDuration(tittle.getDuration());
        }

        if (tittle.getRelease_date() != null) {
            existing.setReleseDate(tittle.getRelease_date());
        }

        if (tittle.getFeatured() != null) {
            existing.setFeatured(tittle.getFeatured());
        }

        if (tittle.getGenre() != null) {
            Set<Genre> genres = new HashSet<>(genreRepository.findAllById(tittle.getGenre()));

            existing.setGenres(genres);
        }

        if (tittle.getPeople() != null) {
            Set<People> people = new HashSet<>(peopleRepository.findAllById(tittle.getPeople()));
            existing.setPeople(people);
        }

        Tittle updated = tittleRepository.save(existing);
        return toResponse(updated);
    }

    @Override
    public Long SumView() {
        return tittleRepository.sumView();
    }

//    @Override
//    public Page<TittleResponse> byviews(Pageable pageable) {
//           return tittleRepository.findAllByOrderByViewsDesc(pageable)
//                .map(this::toResponse);
//    }

    @Override
    public List<TittleResponse> findByPeople(Long Id) {
        People a=peopleRepository.getPeopleById(Id).orElseThrow(() -> new RuntimeException("No people found"));
        return tittleRepository.findByPeopleContaining(a).stream().map(this::toResponse).toList();


    }

    @Override
    public List<Tittle> findByName(String Name) {
        return tittleRepository.findAllByName(Name);
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
    public void addviews(Long id) {
        Tittle a=tittleRepository.findById(id).orElseThrow(()->new RuntimeException("ko tim thay"));
        if(!watchHistoryRepository.existsByTittle(a)){
            a.setViews(a.getViews()+1);
            tittleRepository.save(a);
        }


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

    @Override
    public int getview(Long Id) {
      Tittle a=tittleRepository.findById(Id).orElseThrow(() -> new RuntimeException("No tittle found"));
      return a.getViews();
    }

    @Override
    public Long getviews() {
        return tittleRepository.sumView();
    }

    @Override
    public void watch(Long Id) {
        Tittle a=tittleRepository.findById(Id).orElseThrow(() -> new RuntimeException("No tittle found"));
        a.setViews(a.getViews() + 1);
        tittleRepository.save(a);
    }

    private TittleResponse toResponse(Tittle tittle) {
        return new TittleResponse(
                tittle.getId(),
                tittle.getName(),
                tittle.getOriginalName(),

                tittle.getDescription(),
                tittle.getCountry(),
                tittle.getDuration(),
                tittle.getReleseDate(),
                String.valueOf(tittle.getTitleType()),
                tittle.getViews(),
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