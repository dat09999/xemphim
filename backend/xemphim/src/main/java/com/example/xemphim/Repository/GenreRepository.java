package com.example.xemphim.Repository;

import com.example.xemphim.Entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface GenreRepository  extends JpaRepository<Genre, Long> {

}
