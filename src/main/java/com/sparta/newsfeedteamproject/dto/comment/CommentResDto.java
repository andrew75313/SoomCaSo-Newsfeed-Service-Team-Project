package com.sparta.newsfeedteamproject.dto.comment;

import com.sparta.newsfeedteamproject.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResDto {
    private Long id;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String username;
    private Long feedId;

    public CommentResDto(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.feedId = comment.getFeed().getId();
        this.username = comment.getUser().getUsername();
    }

}
