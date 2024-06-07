package com.sparta.newsfeedteamproject.dto;

import com.sparta.newsfeedteamproject.entity.Contents;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeResDto {

    private Long contentsId;
    private Contents contentsType;

    public LikeResDto(Long contentsId, Contents contents) {
        this.contentsId = contentsId;
        this.contentsType = contents;
    }
}
