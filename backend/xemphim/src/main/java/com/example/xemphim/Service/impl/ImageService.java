package com.example.xemphim.Service.impl;

import com.example.xemphim.DTO.MovieLink.MovieLinkReponse;
import com.example.xemphim.DTO.image.ImageReponse;
import com.example.xemphim.Entity.Image;
import com.example.xemphim.Entity.MovieLink;
import com.example.xemphim.Entity.Tittle;
import com.example.xemphim.Enum.Img;
import com.example.xemphim.Repository.ImageRepository;
import com.example.xemphim.Repository.MovieLinkRepository;
import com.example.xemphim.Repository.TittleRepository;
import com.example.xemphim.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor


public class ImageService {
    @Value("${minio.bucket}")
    private String bucket;
    @Value("${minio.bucket.video}")
    private String bucket1;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ObjectStorageService storage;
    private final TittleRepository tittleRepository;
    private final MovieLinkRepository movieLinkRepository;
    private final     HlsService hlsService;




    @Transactional
    public ImageReponse uploadPoster(String email, MultipartFile file, String Kind) throws Exception {

         var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String ext = getExt(file.getOriginalFilename());   // ".jpg"
        String key = "user/" + user.getId() + "/" + java.util.UUID.randomUUID() + ext;

        storage.putObject(bucket, key, file);
        imageRepository.deleteAllByOwnerID(Long.valueOf(user.getId()));
        Image a=Image.builder()

                .path(key)
                .kind(Enum.valueOf(Img.class,Kind))
                .ownerID(Long.valueOf(user.getId()))
                .created(LocalDateTime.now())
                .build();
        imageRepository.save(a);
        // trả luôn posterUrl để frontend show ngay
        String posterUrl = storage.getPublicUrlOrSignedUrl(bucket, key);

        return new ImageReponse(posterUrl, Math.toIntExact(a.getOwnerID()),a.getKind());
    }
    @Transactional
    public ImageReponse uploadimage(Long Id, MultipartFile file, String Kind) throws Exception {

       Tittle a1=tittleRepository.findById(Id).orElseThrow(() -> new RuntimeException("Tittle not found"));

        String ext = getExt(file.getOriginalFilename());   // ".jpg"
        String key = "user/" + a1.getId() + "/" + java.util.UUID.randomUUID() + ext;

        storage.putObject(bucket1, key, file);

        Image a=Image.builder()

                .path(key)
                .kind(Enum.valueOf(Img.class,Kind))
                .ownerID(a1.getId())
                .created(LocalDateTime.now())
                .build();
        imageRepository.save(a);
        // trả luôn posterUrl để frontend show ngay
        String posterUrl = storage.getPublicUrlOrSignedUrl(bucket1, key);

        return new ImageReponse(posterUrl, Math.toIntExact(a.getOwnerID()),a.getKind());
    }



        public MovieLinkReponse uploadVideo(Long id, MultipartFile file) throws Exception {
            Tittle a1 = tittleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tittle not found"));

            // Dùng UUID làm videoId
            String videoId = UUID.randomUUID().toString();

            // Convert sang HLS + AES, upload lên MinIO

            String m3u8Path = hlsService.convertAndUpload(file, videoId);

            // Lưu path m3u8 vào DB (không phải link trực tiếp nữa)
            MovieLink movieLink = MovieLink.builder()
                    .link(videoId) // Lưu videoId để sau query
                    .tittle(a1)
                    .build();
            movieLinkRepository.save(movieLink);

            return new MovieLinkReponse(videoId); // Trả videoId cho frontend
        }



    private String getExt(String name) {
        if (name == null || !name.contains(".")) return ".jpg";
        return name.substring(name.lastIndexOf("."));
    }
}