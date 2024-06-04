package com.sparta.newsfeedteamproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
@RequiredArgsConstructor
public class User extends Timestamp{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String login_id;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column
    private String user_info;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column
    private String refresh_token;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime status_mod_time;

    public User(String loginId, String password, String name, String email, String userInfo, Status status) {
        this.login_id = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.user_info = userInfo;
        this.status = status;
    }
}
