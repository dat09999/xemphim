package com.example.xemphim.DTO.Comment;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private Long tittleId;
    private Long userId;
    private Long parentCommentId;
    private String content;
    private Date date;
}