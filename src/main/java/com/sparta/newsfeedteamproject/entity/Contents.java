package com.sparta.newsfeedteamproject.entity;

public enum Contents {

    FEED(ContentsType.FEED),
    COMMENT(ContentsType.COMMENT);

    private final String contents;

    Contents(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return this.contents;
    }

    public static class ContentsType {
        public static final String FEED = "FEED";
        public static final String COMMENT = "COMMENT";
    }
}
