package com.example.xemphim.Repository;

import com.example.xemphim.Entity.People;
import com.example.xemphim.Entity.Tittle;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TittleRepository extends JpaRepository<Tittle, Long> {

    @Query("""
        select distinct t
        from Tittle t
        join t.genres g
        where lower(g.name) = lower(:genre)
    """)
    List<Tittle> findByGenreNameIgnoreCase(@Param("genre") String genre);

    List<Tittle> findAllByCountry(String country);

    @NotNull
    Optional<Tittle> findById(@NotNull Long Id);

    List<Tittle> findAllByReleseDateBetween(LocalDate start, LocalDate end);

    List<Tittle> findAllByOrderByReleseDateDesc(Pageable pageable);

    List<Tittle> findAllByFeaturedTrueOrderByReleseDateDesc(Pageable pageable);

    List<Tittle> findByPeopleContaining(People people);
}