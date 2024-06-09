package com.sparta.newsfeedteamproject.dto.comment;

import lombok.Getter;

@Getter
public class CommentDelResDto {

    private Long id;

    public CommentDelResDto(Long id) {
        this.id = id;
    }
}
