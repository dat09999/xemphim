package com.example.xemphim.Controller;

import com.example.xemphim.DTO.Comment.CommentRequest;
import com.example.xemphim.DTO.Comment.CommentResponse;
import com.example.xemphim.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest request) {
        CommentResponse response = commentService.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long id,
                                                         @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Xóa comment thành công");
    }

    @GetMapping("/tittle/{tittleId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByTittleId(@PathVariable Long tittleId) {
        return ResponseEntity.ok(commentService.getCommentsByTittleId(tittleId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(commentService.getCommentsByUserId(userId));
    }
    @GetMapping("/count")
    public ResponseEntity<Long> getCommentsByUserId() {
        return ResponseEntity.ok(commentService.Count());
    }
    @GetMapping("/count/tittle")
    public ResponseEntity<Long> getCommentsByTitleId(@RequestParam Long Id) {
        return ResponseEntity.ok(commentService.CountByTittleId(Id));
    }

    @GetMapping("/parent-comment/{parentCommentId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByParentCommentId(@PathVariable Long parentCommentId) {
        return ResponseEntity.ok(commentService.getCommentsByParentCommentId(parentCommentId));
    }
}