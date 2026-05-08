package com.example.xemphim.Service.impl;

import com.example.xemphim.DTO.Favor.FavourRankingDTO;
import com.example.xemphim.DTO.FavourDTO;
import com.example.xemphim.Entity.Tittle;
import com.example.xemphim.Entity.User;
import com.example.xemphim.Entity.favour;
import com.example.xemphim.Repository.FavourRepository;
import com.example.xemphim.Repository.TittleRepository;
import com.example.xemphim.Repository.UserRepository;
import com.example.xemphim.Service.FavourService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.example.xemphim.DTO.Favor.FavourSort;

@RequiredArgsConstructor
@Service
public class FavourServiceImpl implements FavourService {
    private final FavourRepository favourRepository;
    private  final UserRepository userRepository;
    private final TittleRepository tittleRepository;
    @Override
    @Transactional
    public FavourDTO addFavour(Integer userId, Long tittleId) {
        if (favourRepository.existsByUser_IdAndTittle_Id(userId, tittleId)) {
            throw new RuntimeException("lỗi");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("không tìm thấy user: " + userId));
        Tittle tittle = tittleRepository.findById(tittleId)
                .orElseThrow(() -> new RuntimeException("không tìm thấy phim: " + tittleId));

        favour newFavour = new favour();
        newFavour.setUser(user);
        newFavour.setTittle(tittle);
        newFavour.setCreateDate(LocalDateTime.now());

        return toDTO(favourRepository.save(newFavour));
    }

    @Override
    public Long countBytitile(Long tittleId) {
        return favourRepository.countByTittleId(tittleId);
    }

    @Override
    @Transactional
    public void removeFavour(Integer userId, Long tittleId) {
        if (!favourRepository.existsByUser_IdAndTittle_Id(userId, tittleId)) {
            throw new RuntimeException("KhÃ´ng tÃ¬m tháº¥y má»¥c yÃªu thÃ­ch.");
        }
        favourRepository.deleteByUser_IdAndTittle_Id(userId, tittleId);
    }

    @Override
    public List<FavourDTO> getFavoursByUser(Integer userId) {
        return favourRepository.findByUser_Id(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFavoured(Integer userId, Long tittleId) {
        return favourRepository.existsByUser_IdAndTittle_Id(userId, tittleId);
    }

    @Override
    public List<FavourRankingDTO> getTopFavouredTittles(int topN) {
        Pageable pageable = PageRequest.of(0, topN);
        return favourRepository.findMostFavouredTittles(pageable)
                .stream()
                .map(row -> {
                    Tittle tittle = (Tittle) row[0];
                    Long count = (Long) row[1];
                    return new FavourRankingDTO(tittle.getId(), tittle.getName(), count);
                })
                .collect(Collectors.toList());
    }
    private FavourDTO toDTO(favour f) {
        return new FavourDTO(
                f.getId(),
                f.getTittle().getId(),
                f.getTittle().getName(),
                f.getCreateDate()
        );
    }

    @Override

    public Page<FavourSort> getFavourStatistics(Long genreId, String sortDirection, int page, int size) {
        if (sortDirection == null || sortDirection.isBlank()) {
            sortDirection = "DESC";
        }

        sortDirection = sortDirection.toUpperCase();

        if (!sortDirection.equals("ASC") && !sortDirection.equals("DESC")) {
            sortDirection = "DESC";
        }

        page = Math.max(page, 0);
        size = Math.max(size, 1);
        size = Math.min(size, 50);

        Pageable pageable = PageRequest.of(page, size);

        return favourRepository.findAllTittlesWithFavourCount(
                genreId,
                sortDirection,
                pageable
        );
    }
}
