    package com.example.xemphim.Controller;

    import com.example.xemphim.DTO.WatchHistory.WatchHistoryReponse;
    import com.example.xemphim.Entity.User;
    import com.example.xemphim.Repository.UserRepository;
    import com.example.xemphim.Service.impl.WatchHistoryServiceimpl;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.Authentication;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/history")
    @Slf4j
    @RequiredArgsConstructor
    public class WatchController {
        private final WatchHistoryServiceimpl watchService;
        private final UserRepository userRepository;
        @PostMapping("/add/{id}")
        public ResponseEntity<String> add(@PathVariable("id") Long id, Authentication auth) {
            if (auth == null) {
                return ResponseEntity.status(401).body("Chưa đăng nhập");
            }

            String email = auth.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User Not Found"));

            watchService.add(id, user.getId());
            return ResponseEntity.ok("Đã lưu lịch sử xem");


        }
        @GetMapping("/get")
        public ResponseEntity<List<WatchHistoryReponse>> get(Authentication auth) {
            User user = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new RuntimeException("User Not Found"));
            log.info("lay lich su phim");
            return ResponseEntity.ok(watchService.get(user.getId()));

        }
    }
