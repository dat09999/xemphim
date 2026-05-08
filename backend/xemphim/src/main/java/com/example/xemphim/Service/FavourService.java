package com.example.xemphim.Service;

import com.example.xemphim.DTO.Favor.FavourRankingDTO;
import com.example.xemphim.DTO.FavourDTO;
import jakarta.transaction.Transactional;
import com.example.xemphim.DTO.Favor.FavourSort;
import org.springframework.data.domain.Page;


import java.util.List;

public interface FavourService {


    FavourDTO addFavour(Integer userId, Long tittleId);
    Long countBytitile(Long tittleId);


    void removeFavour(Integer userId, Long tittleId);


    List<FavourDTO> getFavoursByUser(Integer userId);


    boolean isFavoured(Integer userId, Long tittleId);


    List<FavourRankingDTO> getTopFavouredTittles(int topN);


    Page<FavourSort> getFavourStatistics(Long genreId, String sortDirection, int page, int size);
}