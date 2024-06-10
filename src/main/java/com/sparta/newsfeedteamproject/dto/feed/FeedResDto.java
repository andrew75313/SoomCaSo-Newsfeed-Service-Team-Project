package com.sparta.newsfeedteamproject.dto.feed;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.newsfeedteamproject.dto.comment.CommentResDto;
import com.sparta.newsfeedteamproject.entity.Feed;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedResDto {

    private Long id;
    private String username;
    private String contents;
    private Long likes;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResDto> comments;

    public FeedResDto(Feed feed) {

        this.id = feed.getId();
        this.username = feed.getUser().getUsername();
        this.contents = feed.getContents();
        this.likes = feed.getLikes();
        this.createdAt = feed.getCreatedAt();
        this.modifiedAt = feed.getModifiedAt();
    }

    public void setComments(List<CommentResDto> comments) {
        this.comments = comments;
    }
}
