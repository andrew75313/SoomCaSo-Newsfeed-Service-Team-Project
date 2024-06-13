package com.sparta.newsfeedteamproject.dto.feed;

import com.sparta.newsfeedteamproject.dto.comment.CommentResDto;
import com.sparta.newsfeedteamproject.entity.Feed;
import com.sparta.newsfeedteamproject.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FeedResDtoTest {
    @Test
    @DisplayName("FeedResDto setComments test")
    public void test(){
        //given
        List<CommentResDto> comments = new ArrayList<>();
        FeedResDto feedResDto = Mockito.mock(FeedResDto.class);

        //when
        feedResDto.setComments(comments);

        //then
        assertEquals(comments, feedResDto.getComments());
    }
}