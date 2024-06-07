package com.sparta.newsfeedteamproject.exception;

public enum ExceptionMessage {

    DIFFERENT_WRITER("해당 게시물은 작성자만 수정/삭제 할 수 있습니다!"),
    DEATIVATE_USER("탈퇴한 회원입니다.");

    private String message;

    private ExceptionMessage(String message) {
        this.message = message;
    }

    public String getExceptionMessage(){
        return this.message;
    }
}
