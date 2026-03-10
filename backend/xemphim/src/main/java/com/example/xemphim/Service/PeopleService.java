package com.example.xemphim.Service;

import com.example.xemphim.DTO.people.PeopleRequest;
import com.example.xemphim.DTO.people.PeopleResponse;
import com.example.xemphim.Entity.People;

import java.util.List;

public interface PeopleService {
   public PeopleResponse getPeople(Long id);
   public void AddPeople(PeopleRequest people);
 public List<PeopleResponse> getAllPeople(String Role);
 public List<PeopleResponse> getAllPeople();

}
