package com.example.xemphim.Service;

import com.example.xemphim.DTO.WatchHistory.WatchHistoryReponse;

import java.util.List;

public interface WatchHistoryService {

    public void add(Long id,int id2) ;
    public List<WatchHistoryReponse> get(int id);


}
