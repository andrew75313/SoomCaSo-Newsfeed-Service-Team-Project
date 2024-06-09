package com.sparta.newsfeedteamproject.entity;

public enum Status {

    UNAUTHORIZED(UserStatus.UNAUTHORIZED),
    ACTIVATE(UserStatus.ACTIVATE),
    DEACTIVATE(UserStatus.DEACTIVATE);

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public static class UserStatus {
        public static final String UNAUTHORIZED = "UNAUTHORIZED";
        public static final String ACTIVATE = "ACTIVATE";
        public static final String DEACTIVATE = "DEACTIVATE";
    }
}
