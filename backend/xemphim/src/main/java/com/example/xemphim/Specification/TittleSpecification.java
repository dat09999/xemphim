package com.example.xemphim.Specification;

import com.example.xemphim.DTO.Tittle.TittleFilterRequest;
import com.example.xemphim.Entity.Genre;
import com.example.xemphim.Entity.Tittle;
import com.example.xemphim.Enum.TitleStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TittleSpecification {

    // cho user: chỉ lấy bài/phim đã publish
    public static Specification<Tittle> filterPublished(TittleFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = buildCommonPredicates(filter, root, query, cb);

            predicates.add(
                    cb.equal(root.get("titleStatus"), TitleStatus.PUBLISHED)
            );

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // cho admin: lấy tất cả, không lọc status
    public static Specification<Tittle> filterForAdmin(TittleFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = buildCommonPredicates(filter, root, query, cb);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static List<Predicate> buildCommonPredicates(
            TittleFilterRequest filter,
            jakarta.persistence.criteria.Root<Tittle> root,
            jakarta.persistence.criteria.CriteriaQuery<?> query,
            jakarta.persistence.criteria.CriteriaBuilder cb
    ) {
        List<Predicate> predicates = new ArrayList<>();

        query.distinct(true);

        if (filter.getYear() != null) {
            predicates.add(
                    cb.equal(
                            cb.function("year", Integer.class, root.get("releseDate")),
                            filter.getYear()
                    )
            );
        }
        if (filter.getName() != null && !filter.getName().isBlank()) {
            predicates.add(
                    cb.like(
                            cb.lower(root.get("name")),
                            "%" + filter.getName().trim().toLowerCase() + "%"
                    )
            );
        }

        if (filter.getCountry() != null && !filter.getCountry().isBlank()) {
            predicates.add(
                    cb.equal(
                            cb.lower(root.get("country")),
                            filter.getCountry().trim().toLowerCase()
                    )
            );
        }

        if (filter.getTittleType() != null) {
            predicates.add(
                    cb.equal(root.get("titleType"), filter.getTittleType())
            );
        }

        if (filter.getGenre() != null && !filter.getGenre().isBlank()) {
            Join<Tittle, Genre> genreJoin = root.join("genres", JoinType.INNER);
            predicates.add(
                    cb.equal(
                            cb.lower(genreJoin.get("name")),
                            filter.getGenre().trim().toLowerCase()
                    )
            );
        }

        return predicates;
    }
}