package com.sparta.newsfeedteamproject.entity;

import com.sparta.newsfeedteamproject.dto.comment.CommentReqDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentTest {

    @Test
    @DisplayName("comment update test")
    public void test() {
        //given
        String contents = "contents";
        Comment comment = new Comment();

        //when
        comment.update(contents);

        //then
        assertEquals(contents,comment.getContents());

    }

    @Test
    @DisplayName("comment increaseLikes test")
    public void test2(){
        //given
        CommentReqDto requestDto = new CommentReqDto();
        Feed feed = new Feed();
        User user = new User();
        Comment comment = new Comment(requestDto,feed,user,0L);
        Long oldLikes = comment.getLikes();

        //when
        comment.increaseLikes();

        //then
        assertEquals(oldLikes+1,comment.getLikes());
    }

    @Test
    @DisplayName("comment decreaseLikes test")
    public void test3(){
        //given
        CommentReqDto requestDto = Mockito.mock(CommentReqDto.class);
        Feed feed = new Feed();
        User user = new User();
        Comment comment = new Comment(requestDto,feed,user,0L);
        Long oldLikes = comment.getLikes();

        //when
        comment.decreaseLikes();

        //then
        assertEquals(oldLikes-1,comment.getLikes());
    }

}