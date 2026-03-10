package com.example.xemphim.Controller;

import com.example.xemphim.Entity.MovieLink;
import com.example.xemphim.Repository.MovieLinkRepository;
import com.example.xemphim.Service.impl.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
@RequiredArgsConstructor
@Slf4j
public class MovieLinkController {

    @Value("${minio.bucket1}")
    private String bucket1;

    private final MovieLinkRepository movieLinkRepository;
    private final ObjectStorageService objectStorageService;
    @PostMapping(value = "/video/{id}")
    private String getVideoLink(@PathVariable("id") Long id) {
        MovieLink a=movieLinkRepository.findByTittle_Id(id);
        log.info("yeu cau video thanh cong");
        return objectStorageService.getPublicUrlOrSignedUrl(bucket1,a.getLink());
    }
}
