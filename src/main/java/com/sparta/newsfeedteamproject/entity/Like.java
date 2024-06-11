package com.sparta.newsfeedteamproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "likes")
@NoArgsConstructor
public class Like extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false, name = "contents_type")
    @Enumerated(EnumType.STRING)
    private Contents contents;
    @Column(nullable = false, name = "contents_id")
    private Long contentsId;

    public Like(User user, Long contentsId, Contents contents) {
        this.user = user;
        this.contents = contents;
        this.contentsId = contentsId;
    }
}
