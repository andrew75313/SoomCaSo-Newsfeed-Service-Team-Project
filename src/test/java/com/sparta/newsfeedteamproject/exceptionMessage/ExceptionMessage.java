package com.sparta.newsfeedteamproject.exceptionMessage;

public class ExceptionMessage {

    public static String nullField(String fieldName) {
        return String.format("%s는 null일 수 없습니다.", fieldName);
    }
}
