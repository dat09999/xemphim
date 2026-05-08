package com.example.xemphim.Repository;

import com.example.xemphim.DTO.Favor.FavourSort;
import com.example.xemphim.Entity.favour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FavourRepository extends JpaRepository<favour, Long> {

    List<favour> findByUser_Id(Integer userId);

    Optional<favour> findByUser_IdAndTittle_Id(Integer userId, Long tittleId);

    boolean existsByUser_IdAndTittle_Id(Integer userId, Long tittleId);

    void deleteByUser_IdAndTittle_Id(Integer userId, Long tittleId);


    @Query("SELECT f.tittle, COUNT(f) as favourCount FROM favour f GROUP BY f.tittle ORDER BY favourCount DESC")
    List<Object[]> findMostFavouredTittles(Pageable pageable);


    @Query("SELECT COUNT(f) FROM favour f WHERE f.tittle.id = :tittleId")
    Long countByTittleId(@Param("tittleId") Long tittleId);
    @Query("""
        SELECT new com.example.xemphim.DTO.Favor.FavourSort(
            t.id,
            t.name,
            COUNT(DISTINCT f.id)
        )
        FROM Tittle t
        LEFT JOIN t.genres g
        LEFT JOIN favour f ON f.tittle = t
        WHERE (:genreId IS NULL OR g.id = :genreId)
        GROUP BY t.id, t.name
        ORDER BY
            CASE WHEN :sortDirection = 'ASC' THEN COUNT(DISTINCT f.id) END ASC,
            CASE WHEN :sortDirection = 'DESC' THEN COUNT(DISTINCT f.id) END DESC,
            t.name ASC
        """)
    Page<FavourSort> findAllTittlesWithFavourCount(
            @Param("genreId") Long genreId,
            @Param("sortDirection") String sortDirection,
            Pageable pageable
    );
}