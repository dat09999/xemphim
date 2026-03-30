package com.example.xemphim.DTO.Comment;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private Long tittleId;
    private String tittleName;
    private Long userId;
    private String username;
    private Long parentCommentId;
    private String parentUsername;
    private String content;
    private Date date;
}