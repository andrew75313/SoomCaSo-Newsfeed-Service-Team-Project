package com.sparta.newsfeedteamproject.dto.feed;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FeedReqDto {

    @NotBlank
    private String contents;
}