package com.sparta.newsfeedteamproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResDto {

    private String msg;
    private int status_code;

    public BaseResDto(String msg, int status_code) {
        this.msg = msg;
        this.status_code = status_code;
    }
}
