package com.sparta.newsfeedteamproject.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BaseResDto<T> {

    private int statusCode;
    private String message;
    private T data;

    public BaseResDto(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public BaseResDto(int statusCode,String message) {
        this.statusCode = statusCode;
        this.message = message;
    }


}
