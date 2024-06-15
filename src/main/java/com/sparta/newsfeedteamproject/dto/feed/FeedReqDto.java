package com.sparta.newsfeedteamproject.dto.feed;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedReqDto {

    @NotBlank(message = "contents should not be blank")
    private String contents;
}