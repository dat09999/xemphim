package com.example.xemphim.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.util.Date;

@Entity
public class Comment {
    @Id
    private Long id;
    @OneToOne
    @JoinColumn(name = "tittle_id", referencedColumnName = "id")
    private Tittle tittle;
    @OneToOne
    @JoinColumn(name = "User_id", referencedColumnName = "id",nullable = false)
    private User user;
    @OneToOne
    @JoinColumn(name = "ParentUser_id", referencedColumnName = "id")
    private User parentUser;
    private String content;
    private Date date;

}
