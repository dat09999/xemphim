package com.example.xemphim.Controller;

import com.example.xemphim.DTO.Tittle.TittleRequest;
import com.example.xemphim.DTO.Tittle.TittleResponse;
import com.example.xemphim.Service.TittleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tittles")
@RequiredArgsConstructor
public class TittleController {

    private final TittleService tittleService;

    @PostMapping
    public ResponseEntity<String> add(@RequestBody TittleRequest tittleRequest) {
        tittleService.add(tittleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Add tittle successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<TittleResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tittleService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TittleResponse>> findAll() {
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

    @GetMapping("/year")
    public ResponseEntity<List<TittleResponse>> findByYear(@RequestParam int year) {
        return ResponseEntity.ok(tittleService.findByYear(year));
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
}