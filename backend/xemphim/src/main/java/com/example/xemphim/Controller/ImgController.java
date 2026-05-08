package com.example.xemphim.Controller;

import com.example.xemphim.DTO.MovieLink.MovieLinkReponse;
import com.example.xemphim.DTO.image.ImageReponse;
import com.example.xemphim.Entity.Image;
import com.example.xemphim.Entity.User;
import com.example.xemphim.Enum.Img;
import com.example.xemphim.Repository.ImageRepository;
import com.example.xemphim.Repository.UserRepository;
import com.example.xemphim.Service.impl.ImageService;
import com.example.xemphim.Service.impl.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
@Slf4j
public class ImgController {
    @Value("${minio.bucket}")
    private String bucket;
    @Value("${minio.bucket1}")
    private String bucket1;
    private final ImageService imageService;
    private  final ImageRepository imageRepository;
    private  final UserRepository userRepository;
    private  final ObjectStorageService objectStorageService;

    @PostMapping(value="/upload/user", consumes = "multipart/form-data")
    public ImageReponse uploadPoster( Authentication authentication,
                                     @RequestPart("file") MultipartFile file,@RequestParam String Kind) throws Exception {

        return imageService.uploadPoster(authentication.getName(), file,Kind);

    }
    @PostMapping(value = "/upload/image/{id}", consumes = "multipart/form-data")
    public ImageReponse uploadImage(
            @PathVariable("id") Long id,
            @RequestPart("file") MultipartFile file,@RequestParam String Kind) throws Exception {

        return imageService.uploadimage(id, file, Kind);
    }

    @PostMapping(value = "/upload/video/{id}", consumes = "multipart/form-data")
    public MovieLinkReponse uploadVideo(
            @PathVariable("id") Long id,
            @RequestPart("file") MultipartFile file) throws Exception {

        return imageService.uploadVideo(id, file);
    }
    @GetMapping("/image/user")
    public String getUserImage(Authentication authentication) throws Exception {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(()->new RuntimeException("User Not Found"));

        Image img =imageRepository.findByOwnerIDAndKind(Long.valueOf(user.getId()), Img.valueOf("USER")).orElseThrow(()->new RuntimeException("User Not Found"));
        log.info("lay anh thanh cong");
        return objectStorageService.getPublicUrlOrSignedUrl(bucket,img.getPath());
    }

    @GetMapping("/image/video/{id}")
    public String getVideoImage(
            @PathVariable("id") Long titleID,
            @RequestParam String kind) throws Exception {

        Img imgKind = Img.valueOf(kind.toUpperCase());

        Image img = imageRepository.findByOwnerIDAndKind(titleID, imgKind)
                .orElseThrow(() -> new RuntimeException("Image Not Found"+titleID));

        log.info("lay anh thanh cong");

        String url= objectStorageService.getPublicUrlOrSignedUrl(bucket1, img.getPath());
        log.info(url);
        return url;
    }


}