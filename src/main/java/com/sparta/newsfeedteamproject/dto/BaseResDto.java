package com.sparta.newsfeedteamproject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BaseResDto<T> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int statusCode;
    private String message;
    private T data;

    public BaseResDto(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
