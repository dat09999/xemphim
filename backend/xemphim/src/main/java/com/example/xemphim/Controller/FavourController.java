package com.example.xemphim.Controller;

import com.example.xemphim.DTO.Favor.FavourRankingDTO;
import com.example.xemphim.DTO.FavourDTO;

import com.example.xemphim.Entity.User;
import com.example.xemphim.Repository.UserRepository;
import com.example.xemphim.Service.FavourService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.xemphim.DTO.Favor.FavourSort;

import java.util.List;

@RestController
@RequestMapping("/api/favour")
@RequiredArgsConstructor
@Slf4j
public class FavourController {


    private final FavourService favourService;
    private final UserRepository userRepository;


    @PostMapping("/{userId}/{tittleId}")
    public ResponseEntity<FavourDTO> addFavour(@PathVariable Integer userId,
                                               @PathVariable Long tittleId) {
        return ResponseEntity.ok(favourService.addFavour(userId, tittleId));
    }


    @DeleteMapping("/{userId}/{tittleId}")
    public ResponseEntity<Void> removeFavour(@PathVariable Integer userId,
                                             @PathVariable Long tittleId) {
        favourService.removeFavour(userId, tittleId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/user")
    public ResponseEntity<List<FavourDTO>> getFavoursByUser(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new RuntimeException("User Not Found"));
        log.info("duoc roi");
        return ResponseEntity.ok(favourService.getFavoursByUser(user.getId()));
    }


    @GetMapping("/check/{userId}/{tittleId}")
    public ResponseEntity<Boolean> isFavoured(@PathVariable Integer userId,
                                              @PathVariable Long tittleId) {
        return ResponseEntity.ok(favourService.isFavoured(userId, tittleId));
    }


    @GetMapping("/top")
    public ResponseEntity<List<FavourRankingDTO>> getTopFavouredTittles(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(favourService.getTopFavouredTittles(limit));
    }
    @GetMapping("/count")
    public ResponseEntity<?> count(@RequestParam Long id
            ) {
        return ResponseEntity.ok(favourService.countBytitile(id));
    }
    @GetMapping("/admin/statistics")
    public ResponseEntity<Page<FavourSort>> getFavourStatistics(
            @RequestParam(required = false) Long genreId,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(
                favourService.getFavourStatistics(genreId, sortDirection, page, size)
        );
    }
}