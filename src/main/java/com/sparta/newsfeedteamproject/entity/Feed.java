package com.sparta.newsfeedteamproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Getter
@Table(name = "NewsFeed") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Feed extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "contents", nullable = false)
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
