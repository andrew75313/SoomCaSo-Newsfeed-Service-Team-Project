package com.sparta.newsfeedteamproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="like")
public class Like extends Timestamp{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false,name = "contents_type")
    @Enumerated(EnumType.STRING)
    private Contents contents;
    @Column(nullable = false, name = "contents_id")
    private Long contentsId;
}
