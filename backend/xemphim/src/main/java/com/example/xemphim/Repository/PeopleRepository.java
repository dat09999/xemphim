package com.example.xemphim.Repository;

import com.example.xemphim.Entity.People;
import com.example.xemphim.Enum.Role_People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PeopleRepository extends JpaRepository<People, Long> {
    Optional<People> getPeopleById(Long id);

    List<People> getAllByRole(Role_People rolePeople);
}
