package com.example.xemphim.Service.impl;

import com.example.xemphim.DTO.Comment.CommentRequest;
import com.example.xemphim.DTO.Comment.CommentResponse;
import com.example.xemphim.Entity.Comment;
import com.example.xemphim.Entity.Tittle;
import com.example.xemphim.Entity.User;
import com.example.xemphim.Repository.CommentRepository;
import com.example.xemphim.Repository.TittleRepository;
import com.example.xemphim.Repository.UserRepository;
import com.example.xemphim.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class commentServiceimpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TittleRepository tittleRepository;
    private final UserRepository userRepository;

    @Override
    public List<CommentResponse> getAllComments() {
        return commentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment không tồn tại với id = " + id));
        return mapToResponse(comment);
    }

    @Override
    public CommentResponse createComment(CommentRequest request) {
        Tittle tittle = tittleRepository.findById(request.getTittleId())
                .orElseThrow(() -> new RuntimeException("Tittle không tồn tại với id = " + request.getTittleId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại với id = " + request.getUserId()));

        Comment parentComment = null;
        if (request.getParentCommentId() != null) {
            parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment không tồn tại với id = " + request.getParentCommentId()));
        }

        Comment comment = new Comment();
        comment.setTittle(tittle);
        comment.setUser(user);
        comment.setParentComment(parentComment);
        comment.setContent(request.getContent());
        comment.setDate(request.getDate() != null ? request.getDate() : new Date());

        return mapToResponse(commentRepository.save(comment));
    }

    @Override
    public CommentResponse updateComment(Long id, CommentRequest request) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment không tồn tại với id = " + id));

        if (request.getTittleId() != null) {
            Tittle tittle = tittleRepository.findById(request.getTittleId())
                    .orElseThrow(() -> new RuntimeException("Tittle không tồn tại với id = " + request.getTittleId()));
            existingComment.setTittle(tittle);
        }

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User không tồn tại với id = " + request.getUserId()));
            existingComment.setUser(user);
        }

        if (request.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment không tồn tại với id = " + request.getParentCommentId()));
            existingComment.setParentComment(parentComment);
        } else {
            existingComment.setParentComment(null);
        }

        existingComment.setContent(
                request.getContent() != null ? request.getContent() : existingComment.getContent()
        );
        existingComment.setDate(
                request.getDate() != null ? request.getDate() : existingComment.getDate()
        );

        return mapToResponse(commentRepository.save(existingComment));
    }

    @Override
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment không tồn tại với id = " + id);
        }
        commentRepository.deleteById(id);
    }

    @Override
    public Long Count() {
        return commentRepository.count();
    }

    @Override
    public Long CountByTittleId(Long tittleId) {
        return commentRepository.countCommentsByTittle_Id(tittleId);
    }

    @Override
    public List<CommentResponse> getCommentsByTittleId(Long tittleId) {
        Tittle tittle = tittleRepository.findById(tittleId)
                .orElseThrow(() -> new RuntimeException("Tittle không tồn tại với id = " + tittleId));

        return commentRepository.findByTittle(tittle)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getCommentsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại với id = " + userId));

        return commentRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getCommentsByParentCommentId(Long parentCommentId) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("Parent comment không tồn tại với id = " + parentCommentId));

        return commentRepository.findByParentComment(parentComment)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CommentResponse mapToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setDate(comment.getDate());

        if (comment.getTittle() != null) {
            response.setTittleId(comment.getTittle().getId());
            response.setTittleName(comment.getTittle().getName());
        }

        if (comment.getUser() != null) {
            response.setUserId(Long.valueOf(comment.getUser().getId()));
            response.setUsername(comment.getUser().getFullName());
        }

        if (comment.getParentComment() != null) {
            response.setParentCommentId(comment.getParentComment().getId());
        }

        return response;
    }
}