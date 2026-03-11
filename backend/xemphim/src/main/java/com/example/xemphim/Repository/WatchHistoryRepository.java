package com.example.xemphim.Repository;

import com.example.xemphim.Entity.Tittle;
import com.example.xemphim.Entity.User;
import com.example.xemphim.Entity.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface  WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    Optional<WatchHistory> findByTittleAndUser(Tittle b, User a);



    List<WatchHistory> findByUserId(Integer user_id);
}
