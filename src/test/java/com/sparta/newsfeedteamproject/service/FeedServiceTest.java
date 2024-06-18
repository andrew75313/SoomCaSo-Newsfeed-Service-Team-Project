package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.MessageResDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedReqDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedResDto;
import com.sparta.newsfeedteamproject.entity.Comment;
import com.sparta.newsfeedteamproject.entity.Contents;
import com.sparta.newsfeedteamproject.entity.Feed;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.exception.ExceptionMessage;
import com.sparta.newsfeedteamproject.repository.CommentRepository;
import com.sparta.newsfeedteamproject.repository.FeedRepository;
import com.sparta.newsfeedteamproject.repository.LikeRepository;
import com.sparta.newsfeedteamproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    @Mock
    User user;

    @Mock
    Feed feed;

    @InjectMocks
    FeedService feedService;

    @Mock
    FeedRepository feedRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    LikeRepository likeRepository;

    private User differentUser;
    private FeedReqDto reqDto;

    @BeforeEach
    void setUp() {
        reqDto = Mockito.mock(FeedReqDto.class);
        feed = Mockito.mock(Feed.class);
        differentUser = Mockito.mock(User.class);
    }

    @Test
    @DisplayName("게시글 수정 - 로그인 한 유저가 게시글 작성자랑 다를 때")
    public void should_ThrowException_when_UserIsDifferent(){
        //given
        given(feedRepository.findById(any(Long.class))).willReturn(Optional.of(feed));
        when(feed.getUser()).thenReturn(differentUser);
        when(feed.getUser().getId()).thenReturn(1L);
        when(user.getId()).thenReturn(2L);

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () ->{
            feedService.updateFeed(1L,reqDto,user);
        });

        //then
        assertEquals(ExceptionMessage.DIFFERENT_WRITER.getExceptionMessage(), exception.getMessage());
        verify(feed, never()).update(reqDto);

    }

    @Test
    @DisplayName("게시글 수정 - 로그인 한 유저가 게시글 작성자랑 같을 때")
    public void should_UpdateFeed_when_UserIsAuthor(){
        //given
        FeedResDto feedResDto = Mockito.mock(FeedResDto.class);

        when(feedRepository.findById(any(Long.class))).thenReturn(Optional.of(feed));
        when(feed.getUser()).thenReturn(differentUser);
        when(feed.getUser().getId()).thenReturn(1L);
        when(user.getId()).thenReturn(1L);

        //when
        MessageResDto<FeedResDto> messageResDto = feedService.updateFeed(1L,reqDto,user);

        //then
       assertThat(messageResDto.getData().getId()).isEqualTo(0L);
       assertThat(messageResDto.getMessage()).isEqualTo("게시물 수정이 완료되었습니다!");
       assertThat(messageResDto.getStatusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("게시글 수정 - 해당 게시글이 없을 때")
    public void should_ThrowException_when_FeedIsNotExist(){
        //given
        when(feedRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () ->{
            feedService.updateFeed(1L,reqDto,user);
        });

        //then
        assertEquals(ExceptionMessage.NON_EXISTENT_ELEMENT.getExceptionMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("게시글 삭제 - 로그인 한 유저가 게시글 작성자일 때")
    public void should_DeleteFeed_when_UserIsAuthor() {
        //given
        given(feedRepository.findById(any(Long.class))).willReturn(Optional.of(feed));
        when(feed.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(1L);

        List<Comment> comments = Collections.emptyList();
        given(commentRepository.findAllByFeedId(any(Long.class))).willReturn(comments);

        //when
        MessageResDto<FeedResDto> response = feedService.deleteFeed(1L, user);

        //then
        verify(likeRepository, times(1)).findAllByContentsIdAndContents(any(Long.class), eq(Contents.FEED));
        verify(feedRepository, times(1)).delete(feed);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getMessage()).isEqualTo("게시물 삭제가 완료되었습니다!");
        assertThat(response.getData()).isNull();
    }

    @Test
    @DisplayName("게시글 삭제 - 로그인 한 유저가 게시글 작성자가 아닐 때")
    public void should_ThrowException_when_UserIsNotAuthor() {
        //given
        given(feedRepository.findById(anyLong())).willReturn(Optional.of(feed));
        when(feed.getUser()).thenReturn(differentUser);
        when(differentUser.getId()).thenReturn(2L);
        when(user.getId()).thenReturn(1L);

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            feedService.deleteFeed(1L, user);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo(ExceptionMessage.DIFFERENT_WRITER.getExceptionMessage());
        verify(feedRepository, never()).delete(feed);
    }
}

