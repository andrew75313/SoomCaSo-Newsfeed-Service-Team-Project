package com.sparta.newsfeedteamproject.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BaseResDto<T> {

    private Integer statusCode;
    private String message;
    private T data;

    public BaseResDto(Integer statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public BaseResDto(int statusCode,String msg, T data) {
        this.statusCode = statusCode;
        this.msg = msg;
        this.data = data;
    }
}
