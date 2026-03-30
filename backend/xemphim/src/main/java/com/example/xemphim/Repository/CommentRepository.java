package com.example.xemphim.Repository;

import com.example.xemphim.Entity.Comment;
import com.example.xemphim.Entity.User;
import com.example.xemphim.Entity.Tittle;
import jakarta.validation.groups.ConvertGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTittle(Tittle tittle);

    List<Comment> findByUser(User user);

    List<Comment> findByParentComment(Comment parentComment);

    Long countCommentsByTittle_Id(Long tittleId);
}
