package com.sparta.newsfeedteamproject.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommentDelResDto {

    private Long id;

    public CommentDelResDto(Long id) {
        this.id = id;
    }
}
