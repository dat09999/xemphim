package com.example.xemphim.Repository;

import com.example.xemphim.Entity.MovieLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieLinkRepository extends JpaRepository<MovieLink, Long> {


    Optional<  MovieLink> findByTittle_Id(Long tittleId);
}
