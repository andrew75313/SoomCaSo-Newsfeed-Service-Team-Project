package com.sparta.newsfeedteamproject.entity;

import com.sparta.newsfeedteamproject.dto.comment.CommentReqDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedReqDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FeedTest {
    @Test
    @DisplayName("Feed update test")
    public void test(){
        //given
        FeedReqDto reqDto = Mockito.mock(FeedReqDto.class);
        when(reqDto.getContents()).thenReturn("contents");

        Feed feed = new Feed();

        //when
        feed.update(reqDto);

        //then
        assertEquals(reqDto.getContents(), feed.getContents());

    }

    @Test
    @DisplayName("Feed increase test")
    public void test2(){
        //given
        FeedReqDto requestDto = Mockito.mock(FeedReqDto.class);
        User user = Mockito.mock(User.class);
        Feed feed = new Feed(requestDto,user);

        //when
        feed.increaseLikes();

        //then
        assertEquals(1, feed.getLikes());

    }

    @Test
    @DisplayName("Feed decrease test")
    public void test3(){
        //given
        FeedReqDto requestDto = Mockito.mock(FeedReqDto.class);
        User user = Mockito.mock(User.class);
        Feed feed = new Feed(requestDto,user);

        //when
        feed.decreaseLikes();

        //then
        assertEquals(-1, feed.getLikes());

    }
}