package com.example.xemphim.Service.impl;

import com.example.xemphim.DTO.WatchHistory.WatchHistoryReponse;
import com.example.xemphim.Entity.Tittle;
import com.example.xemphim.Entity.User;
import com.example.xemphim.Entity.WatchHistory;
import com.example.xemphim.Repository.TittleRepository;
import com.example.xemphim.Repository.UserRepository;
import com.example.xemphim.Repository.WatchHistoryRepository;
import com.example.xemphim.Service.WatchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchHistoryServiceimpl implements WatchHistoryService {
    private final WatchHistoryRepository watchHistoryRepository;
    private final UserRepository userRepository;
    private final TittleRepository tittleRepository;
    @Override
    public void add(Long id, int id2) {
        User a = userRepository.findById(id2).orElseThrow(()->new RuntimeException("User not found"));
        Tittle b= tittleRepository.findById(id).orElseThrow(()->new RuntimeException("Tittle not found"));
        Optional<WatchHistory> optionalWatchHistory =
                watchHistoryRepository.findByTittleAndUser(b, a);

        WatchHistory watchHistory;

        if (optionalWatchHistory.isPresent()) {
            watchHistory = optionalWatchHistory.get();
            watchHistory.setDate(LocalDateTime.now());
        } else {
            watchHistory = WatchHistory.builder()
                    .tittle(b)
                    .user(a)
                    .date(LocalDateTime.now())
                    .build();
        }

        watchHistoryRepository.save(watchHistory);
    }

    @Override
    public List<WatchHistoryReponse> get(int id) {
     List<WatchHistory> a=watchHistoryRepository.findByUserId(Math.toIntExact(id));
        return a.stream()
                .map(this::convert)
                .toList();

    }
    public WatchHistoryReponse convert(WatchHistory watchHistory) {
        Tittle a=watchHistory.getTittle();

        return WatchHistoryReponse.builder().name(a.getName()).id(a.getId()).date(watchHistory.getDate()).build();




    }
}
