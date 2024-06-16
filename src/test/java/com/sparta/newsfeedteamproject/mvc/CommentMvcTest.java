package com.sparta.newsfeedteamproject.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newsfeedteamproject.config.SecurityConfig;
import com.sparta.newsfeedteamproject.controller.CommentController;
import com.sparta.newsfeedteamproject.controller.FeedController;
import com.sparta.newsfeedteamproject.dto.MessageResDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentDelResDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentReqDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentResDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedResDto;
import com.sparta.newsfeedteamproject.entity.Status;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.security.UserDetailsImpl;
import com.sparta.newsfeedteamproject.service.CommentService;
import com.sparta.newsfeedteamproject.service.FeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        })
class CommentMvcTest {
    private MockMvc mockMvc;
    private Principal mockPrincipal;
    private UserDetailsImpl userDetails;
    private CommentResDto commentResDto;

    @MockBean
    private CommentService commentService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        // Mock 테스트 유저 생성
        String username = "ggumi12345";
        String password = "Ggumi1234567!";
        String name = "김꾸미";
        String email = "ggumi@gmail.com";
        String userInfo = "안녕하세요.";
        Status status = Status.ACTIVATE;
        String refreshToken = "";
        User testUser = new User(username, password, name, email, userInfo, status);
        userDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private void mockResDtoSetup(){
        commentResDto = Mockito.mock(CommentResDto.class);
        when(commentResDto.getId()).thenReturn(1L);
        when(commentResDto.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(commentResDto.getModifiedAt()).thenReturn(LocalDateTime.now());
        when(commentResDto.getUsername()).thenReturn("testUser");
        when(commentResDto.getFeedId()).thenReturn(1L);
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    public void createComment_Ok() throws Exception{
        //given
        this.mockUserSetup();
        this.mockResDtoSetup();

        String contents = "contents";
        CommentReqDto commentReqDto = new CommentReqDto(contents);
        when(commentResDto.getContents()).thenReturn(contents);
        MessageResDto<CommentResDto> messageResDto = Mockito.mock(MessageResDto.class);

        given(commentService.createComment(any(Long.class),any(CommentReqDto.class),any(User.class))).willReturn(messageResDto);
        when(messageResDto.getStatusCode()).thenReturn(200);
        when(messageResDto.getMessage()).thenReturn("댓글 작성이 완료되었습니다!");
        when(messageResDto.getData()).thenReturn(commentResDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/feeds/{feedId}/comments",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentReqDto))
                .principal(mockPrincipal));

        // then
        resultActions
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("댓글 작성이 완료되었습니다!"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.contents").value(contents))
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 생성 실패 테스트 - 댓글 내용이 blank일 때")
    public void should_ThrowException_when_CommentIsBlank() throws Exception{
        //given
        this.mockUserSetup();
        CommentReqDto commentReqDto = Mockito.mock(CommentReqDto.class);
        commentResDto = Mockito.mock(CommentResDto.class);

        // when
        ResultActions resultActions = mockMvc.perform(post("/feeds/{feedId}/comments",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentReqDto))
                .principal(mockPrincipal));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[contents:blank] 댓글 내용을 작성해주세요!"))
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 수정 테스트")
    public void updateComment_Ok() throws Exception{
        //given
        this.mockUserSetup();
        this.mockResDtoSetup();

        String newContents = "new contents";
        CommentReqDto commentReqDto = new CommentReqDto(newContents);
        when(commentResDto.getContents()).thenReturn(newContents);
        MessageResDto<CommentResDto> messageResDto = Mockito.mock(MessageResDto.class);

        given(commentService.updateComment(any(Long.class),any(Long.class),any(CommentReqDto.class),any(User.class))).willReturn(messageResDto);
        when(messageResDto.getStatusCode()).thenReturn(200);
        when(messageResDto.getMessage()).thenReturn("댓글 수정이 완료되었습니다!");
        when(messageResDto.getData()).thenReturn(commentResDto);

        // when
        ResultActions resultActions = mockMvc.perform(put("/feeds/{feedId}/comments/{commentId}",1L,1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentReqDto))
                .principal(mockPrincipal));

        // then
        resultActions
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("댓글 수정이 완료되었습니다!"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.contents").value(newContents))
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 수정 실패 테스트 - 댓글 내용이 blank일 때")
    public void should_ThrowException_when_CommentIsBlank_updateComment() throws Exception{
        //given
        this.mockUserSetup();
        CommentReqDto commentReqDto = Mockito.mock(CommentReqDto.class);
        commentResDto = Mockito.mock(CommentResDto.class);

        // when
        ResultActions resultActions = mockMvc.perform(put("/feeds/{feedId}/comments/{commentId}",1L,1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentReqDto))
                .principal(mockPrincipal));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[contents:blank] 댓글 내용을 작성해주세요!"))
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    public void deleteComment_Ok() throws Exception{
        //given
        this.mockUserSetup();
        this.mockResDtoSetup();

        CommentDelResDto resDto = Mockito.mock(CommentDelResDto.class);
        when(resDto.getId()).thenReturn(1L);
        MessageResDto<CommentDelResDto> messageResDto = Mockito.mock(MessageResDto.class);

        given(commentService.deleteComment(any(Long.class),any(Long.class),any(User.class))).willReturn(messageResDto);
        when(messageResDto.getStatusCode()).thenReturn(200);
        when(messageResDto.getMessage()).thenReturn("댓글 삭제가 완료되었습니다!");
        when(messageResDto.getData()).thenReturn(resDto);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/feeds/{feedId}/comments/{commentId}",1L,1L)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal));

        // then
        resultActions
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("댓글 삭제가 완료되었습니다!"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 조회 테스트")
    public void getComment_Ok() throws Exception{
        //given
        this.mockUserSetup();
        this.mockResDtoSetup();

        String contents = "contents";
        when(commentResDto.getContents()).thenReturn(contents);
        MessageResDto<CommentResDto> messageResDto = Mockito.mock(MessageResDto.class);

        given(commentService.getComment(any(Long.class),any(Long.class))).willReturn(messageResDto);
        when(messageResDto.getStatusCode()).thenReturn(200);
        when(messageResDto.getMessage()).thenReturn("댓글 조회가 완료되었습니다!");
        when(messageResDto.getData()).thenReturn(commentResDto);

        // when
        ResultActions resultActions = mockMvc.perform(get("/feeds/{feedId}/comments/{commentId}",1L,1L)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal));

        // then
        resultActions
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("댓글 조회가 완료되었습니다!"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.contents").value(contents))
                .andDo(print());

    }



}