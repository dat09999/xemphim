package com.example.xemphim.Controller;

import com.example.xemphim.Repository.MovieLinkRepository;
import com.example.xemphim.Service.impl.VideoTokenService;
import com.example.xemphim.security.Jwtservice;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MovieLinkController {

    private final MinioClient minioClient;
    private final VideoTokenService tokenService;
    private final Jwtservice jwtService;
private final MovieLinkRepository movieLinkRepository;
    @Value("${minio.bucket.video}")
    private String videoBucket;

    @Value("${minio.bucket.keys}")
    private String keysBucket;
    @GetMapping("/api/movie/{movieId}/videoId")
    public ResponseEntity<Map<String, String>> getVideoId(
            @PathVariable Long movieId,
            @RequestHeader("Authorization") String authHeader) {

        // Kiểm tra đăng nhập
        jwtService.extractEmail(authHeader.replace("Bearer ", ""));

        // Lấy videoId từ DB
        var movieLink = movieLinkRepository.findByTittle_Id(movieId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        return ResponseEntity.ok(Map.of("videoId",movieLink.getLink()));
    }

    // API 1: Lấy token để xem phim
    @GetMapping("/api/video/{videoId}/token")
    public ResponseEntity<Map<String, String>> getToken(
            @PathVariable String videoId,
            @RequestHeader("Authorization") String authHeader) {

        String userId = jwtService.extractEmail(
                authHeader.replace("Bearer ", "")
        );
        String token = tokenService.generateToken(videoId, userId);
        return ResponseEntity.ok(Map.of("token", token));
    }

    // API 2: Serve file .m3u8 và .ts
    @GetMapping("/api/stream/{videoId}/**")
    public void serveSegment(
            @PathVariable String videoId,
            @RequestParam String token,
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // Validate token + userId
        String currentUserId = jwtService.extractEmail(
                authHeader.replace("Bearer ", "")
        );
        String tokenUserId = tokenService.validateAndGetUserId(token, videoId);

        if (tokenUserId == null || !tokenUserId.equals(currentUserId)) {
            response.setStatus(403);
            return;
        }

        // Lấy tên file từ URL (**) 
        // VD: /api/stream/abc123/index.m3u8 → index.m3u8
        String path = request.getRequestURI();
        String fileName = path.substring(path.lastIndexOf("/") + 1);

        // Lấy file từ MinIO
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(videoBucket)
                        .object("hls/" + videoId + "/" + fileName)
                        .build()
        );

        String contentType = fileName.endsWith(".m3u8")
                ? "application/vnd.apple.mpegurl"
                : "video/MP2T";

        response.setContentType(contentType);
        StreamUtils.copy(stream, response.getOutputStream());
    }

    // API 3: Trả key AES - kiểm tra JWT trước
    @GetMapping("/api/key/{videoId}")
    public void getKey(
            @PathVariable String videoId,
            @RequestParam String token,
            @RequestHeader("Authorization") String authHeader,
            HttpServletResponse response) throws Exception {

        String currentUserId = jwtService.extractEmail(
                authHeader.replace("Bearer ", "")
        );
        String tokenUserId = tokenService.validateAndGetUserId(token, videoId);

        if (tokenUserId == null || !tokenUserId.equals(currentUserId)) {
            response.setStatus(403);
            return;
        }

        // Lấy key từ MinIO bucket private
        InputStream keyStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(keysBucket)
                        .object(videoId + ".key")
                        .build()
        );

        response.setContentType("application/octet-stream");
        StreamUtils.copy(keyStream, response.getOutputStream());
    }
}