package com.example.xemphim.Controller;

import com.example.xemphim.DTO.Tittle.TittleFilterRequest;
import com.example.xemphim.DTO.Tittle.TittleRequest;
import com.example.xemphim.DTO.Tittle.TittleResponse;
import com.example.xemphim.DTO.Tittle.TittleUpDate;
import com.example.xemphim.Entity.Tittle;
import com.example.xemphim.Repository.TittleRepository;
import com.example.xemphim.Service.TittleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tittles")

public class TittleController {

    private final TittleService tittleService;
    private final TittleRepository tittleRepository;

    public TittleController(TittleService tittleService, TittleRepository tittleRepository) {
        this.tittleService = tittleService;
        this.tittleRepository = tittleRepository;
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> add(@RequestBody TittleRequest tittleRequest) {
        tittleService.add(tittleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Add tittle successfully");
    }
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> count(){
        long k=tittleRepository.count();
        return ResponseEntity.status(HttpStatus.OK).body(Long.toString(k));

    }

    @GetMapping("/{id}")
    public ResponseEntity<TittleResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tittleService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TittleResponse>> findAll() {
        log.info("Find all tittles");
        return ResponseEntity.ok(tittleService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        tittleService.delete(id);
        return ResponseEntity.ok("Delete tittle successfully");
    }

    @GetMapping("/genre")
    public ResponseEntity<List<TittleResponse>> findByGenre(@RequestParam String genre) {
        return ResponseEntity.ok(tittleService.findByGenre(genre));
    }

    @GetMapping("/country")
    public ResponseEntity<List<TittleResponse>> findByCountry(@RequestParam String country) {
        return ResponseEntity.ok(tittleService.findByCountry(country));
    }
    @GetMapping("/view")
    public ResponseEntity<Integer> Getview(@RequestParam Long id) {

        return ResponseEntity.ok(tittleService.getview(id));
    }
    @GetMapping("/views")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> Sumview() {

        return ResponseEntity.ok(tittleService.getviews());
    }
    @GetMapping("/year")
    public ResponseEntity<List<TittleResponse>> findByYear(@RequestParam int year) {
        return ResponseEntity.ok(tittleService.findByYear(year));
    }
    @GetMapping("/name")
    public ResponseEntity<List<Tittle>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(tittleService.findByName(name));
    }
    @GetMapping("/newest")
    public ResponseEntity<List<TittleResponse>> getNewest3() {
        return ResponseEntity.ok(tittleService.getNewest3());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<TittleResponse>> getFeatured6() {
        return ResponseEntity.ok(tittleService.getFeatured6());
    }
    @GetMapping("/people")

    public ResponseEntity<List<TittleResponse>> getTittlePeople(@RequestParam Long Id) {
        return ResponseEntity.ok(tittleService.findByPeople(Id));
    }
    @PutMapping("/update")
    public ResponseEntity<TittleResponse> update(@RequestBody TittleUpDate tittleRequest) {
        return ResponseEntity.ok(tittleService.update(tittleRequest));

    }
    @PostMapping("/custom")
    public ResponseEntity<List<TittleResponse>> findByCustom(@RequestBody TittleFilterRequest tittleFilterRequest) {
        log.info(String.valueOf(tittleFilterRequest));
        log.info("tim kiem thanh cong11111111111111111111111111");
        return ResponseEntity.ok(tittleService.filter(tittleFilterRequest));
    }
    @PostMapping("/custom/admin")
    public ResponseEntity<List<TittleResponse>> findByCustomAdmin(@RequestBody TittleFilterRequest tittleFilterRequest) {
        log.info(String.valueOf(tittleFilterRequest));
        log.info("tim kiem thanh cong");
        return ResponseEntity.ok(tittleService.filterAdmin(tittleFilterRequest));
    }
    @PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addMovie(

            @ModelAttribute TittleRequest request,
            @RequestPart(value = "video",required = false)  MultipartFile video,
            @RequestPart(value = "poster",required = false) MultipartFile poster,
            @RequestPart(value = "banner",required = false) MultipartFile banner
    ) {
        log.info(request.getName());
        log.info(video.getOriginalFilename());
        log.info(poster.getOriginalFilename());
        return ResponseEntity.ok(tittleService.addwithLink(request, video, poster, banner));
    }
}