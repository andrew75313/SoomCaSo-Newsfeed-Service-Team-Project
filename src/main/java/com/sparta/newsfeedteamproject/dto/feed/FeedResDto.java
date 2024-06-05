package com.sparta.newsfeedteamproject.dto.feed;

import com.sparta.newsfeedteamproject.entity.Feed;
import com.sparta.newsfeedteamproject.entity.Timestamp;
import lombok.Getter;

@Getter
public class FeedResDto extends Timestamp {

    private Long id;
    private String name;
    private String contents;

    public FeedResDto(Feed feed) {
        this.id = feed.getId();
        this.name = feed.getUser().getName();
        this.contents = feed.getContents();
    }
}
