package com.sparta.newsfeedteamproject.exception;

public enum ExceptionMessage {

    DIFFERENT_WRITER("해당 게시물은 작성자만 수정/삭제 할 수 있습니다!"),
    DEATIVATE_USER("탈퇴한 회원입니다."),
    DUPLICATE_USERNAME("중복된 사용자 이름 입니다."),
    DUPLICATE_EMAIL("중복된 이메일 입니다."),
    INCORRECT_USER("프로필 사용자와 일치하지 않아 요청을 처리할 수 없습니다."),
    INCORRECT_PASSWORD("비밀번호가 일치하지 않아 요청을 처리할 수 없습니다."),
    NOT_FOUND_USER("존재하지 않는 사용자입니다."),
    SAME_PASSWORD("기존 비밀번호와 일치하여 수정이 불가능합니다.");

    private String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getExceptionMessage(){
        return this.message;
    }
}
