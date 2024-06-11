package com.sparta.newsfeedteamproject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageResDto<T> {

    private int statusCode;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public MessageResDto(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
