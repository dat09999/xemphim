package com.example.xemphim.Service.impl;

import com.example.xemphim.DTO.people.PeopleRequest;
import com.example.xemphim.DTO.people.PeopleResponse;
import com.example.xemphim.Entity.People;
import com.example.xemphim.Enum.Role_People;
import com.example.xemphim.Repository.PeopleRepository;
import com.example.xemphim.Service.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PeopleServiceimpl  implements PeopleService {
    private final PeopleRepository peopleRepository;
    @Override
    public PeopleResponse getPeople(Long Id) {
          People people =peopleRepository.getPeopleById(Id).orElseThrow(()->new RuntimeException("People not found"));
        return convertPeople(people);
    }
    public PeopleResponse convertPeople(People a){
        return PeopleResponse.builder()
                .id(a.getId())
                .name(a.getName())
                .description(a.getDescription())
                .birthday(a.getBirthday())
                .gender(a.getGender())
                .country(a.getCountry())
                .role(a.getRole())
                .build();


    }

    @Override
    public void AddPeople(PeopleRequest people) {
              People a=People.builder()
                      .name(people.getFullName())
                      .birthday(people.getDateOfBirth())
                      .Description(people.getDescription())
                      .gender(people.getGender())
                      .role(people.getRole())
                      .build();
              peopleRepository.save(a);

    }

    @Override
    public List<PeopleResponse> getAllPeople(String Role) {
        List<People> a=peopleRepository.getAllByRole(Role_People.valueOf(Role));
        return a.stream().map(this::convertPeople).toList();
    }

    @Override
    public List<PeopleResponse> getAllPeople() {
        List<People>a=peopleRepository.findAll();
        return a.stream().map(this::convertPeople).toList();
    }


}
