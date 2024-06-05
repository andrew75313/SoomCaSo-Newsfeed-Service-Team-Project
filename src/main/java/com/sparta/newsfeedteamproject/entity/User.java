package com.sparta.newsfeedteamproject.entity;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "refresh_token")
    String refreshToken;

    public void deleteRefreshToken() {
         this.refreshToken = "";
    }
}
