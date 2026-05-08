package com.example.xemphim.Repository;

import com.example.xemphim.Entity.MovieLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieLinkRepository extends JpaRepository<MovieLink, Long> {

    MovieLink findByTittle_Id(Long tittleId);
}
