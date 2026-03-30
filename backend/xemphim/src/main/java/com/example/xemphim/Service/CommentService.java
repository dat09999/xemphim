package com.example.xemphim.Service;

import com.example.xemphim.DTO.Comment.CommentRequest;
import com.example.xemphim.DTO.Comment.CommentResponse;

import java.util.List;

public interface CommentService {

    List<CommentResponse> getAllComments();

    CommentResponse getCommentById(Long id);

    CommentResponse createComment(CommentRequest request);

    CommentResponse updateComment(Long id, CommentRequest request);

    void deleteComment(Long id);
    Long Count();
    Long CountByTittleId(Long tittleId);

    List<CommentResponse> getCommentsByTittleId(Long tittleId);

    List<CommentResponse> getCommentsByUserId(Long userId);

    List<CommentResponse> getCommentsByParentCommentId(Long parentCommentId);
}