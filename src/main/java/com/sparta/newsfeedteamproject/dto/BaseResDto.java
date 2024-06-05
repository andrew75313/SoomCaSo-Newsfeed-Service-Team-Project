package com.sparta.newsfeedteamproject.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BaseResDto<T> {

    private int statusCode;
    private String msg;
    private T data;

    public BaseResDto(int statusCode,String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public BaseResDto(int statusCode,String msg, T data) {
        this.statusCode = statusCode;
        this.msg = msg;
        this.data = data;
    }
}
