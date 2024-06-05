package com.sparta.newsfeedteamproject.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BaseResDto<T> {

    private int statusCode;
    private String msg;
    private T data;

    public BaseResDto(int statusCode,String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }
}
