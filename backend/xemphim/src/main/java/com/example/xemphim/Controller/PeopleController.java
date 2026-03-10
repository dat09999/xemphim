package com.example.xemphim.Controller;

import com.example.xemphim.DTO.people.PeopleRequest;
import com.example.xemphim.DTO.people.PeopleResponse;
import com.example.xemphim.Entity.People;
import com.example.xemphim.Service.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/people")
@RequiredArgsConstructor
public class PeopleController {

    private final PeopleService peopleService;

    @GetMapping("/{id}")
    public ResponseEntity<PeopleResponse> getPeople(@PathVariable Long id) {

        return ResponseEntity.ok(peopleService.getPeople(id));
    }

    @PostMapping
    public ResponseEntity<String> addPeople(@RequestBody PeopleRequest peopleRequest) {
        peopleService.AddPeople(peopleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Add people successfully");
    }

    @GetMapping
    public ResponseEntity<List<PeopleResponse>> getAllPeople() {
        return ResponseEntity.ok(peopleService.getAllPeople());
    }

    @GetMapping("/role")
    public ResponseEntity<List<PeopleResponse>> getAllPeopleByRole(@RequestParam String role) {
        return ResponseEntity.ok(peopleService.getAllPeople(role));
    }
}