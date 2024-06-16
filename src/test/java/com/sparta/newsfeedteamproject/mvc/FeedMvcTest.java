package com.sparta.newsfeedteamproject.mvc;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newsfeedteamproject.config.SecurityConfig;
import com.sparta.newsfeedteamproject.controller.FeedController;
import com.sparta.newsfeedteamproject.dto.MessageResDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentResDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedReqDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedResDto;
import com.sparta.newsfeedteamproject.entity.Status;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.security.UserDetailsImpl;
import com.sparta.newsfeedteamproject.service.FeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {FeedController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        })
@AutoConfigureMockMvc(addFilters = false)
public class FeedMvcTest {

    private MockMvc mockMvc;
    private Principal mockPrincipal;
    private UserDetailsImpl userDetails;
    private FeedResDto feedResDto;

    @MockBean
    private FeedService feedService;

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

    private void mockResDtoSetup() {
        //Mock ResDto 테스트 설정
        CommentResDto commentResDto = new CommentResDto(1L, "contents", LocalDateTime.now(), LocalDateTime.now(), "username", 1L);
        List<CommentResDto> commentResDtoList = new ArrayList<>();
        commentResDtoList.add(commentResDto);
        feedResDto = new FeedResDto(1L, "username", "contents", 1L, LocalDateTime.now(), LocalDateTime.now(), commentResDtoList);
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

    //테스트의 관심사 : Request와 그에 따른 Response

    @Test
    @DisplayName("피드 생성 성공 테스트")
    public void createFeed_Ok() throws Exception {
        // given
        this.mockUserSetup();
        this.mockResDtoSetup();
        String contents = "contents";
        FeedReqDto reqDto = new FeedReqDto(contents);
        MessageResDto<FeedResDto> messageResDto = new MessageResDto<>(HttpStatus.OK.value(), "게시물 작성이 완료되었습니다!", feedResDto);

        when(feedService.createFeed(any(FeedReqDto.class), any(User.class))).thenReturn(messageResDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/feeds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto))
                        .principal(mockPrincipal));

        // then
        resultActions
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("게시물 작성이 완료되었습니다!"))
                .andDo(print());

    }

    @Test
    @DisplayName("피드 생성 실패 테스트 - contents가 없는 경우")
    public void should_ThrowException_when_IfcontensIsMissing() throws Exception {
        // given
        this.mockUserSetup();
        String contents = "";
        FeedReqDto reqDto = new FeedReqDto(contents);

        // when
        ResultActions resultActions = mockMvc.perform(post("/feeds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqDto))
                .principal(mockPrincipal));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().string("contents should not be blank"))
                .andDo(print());
    }

    @Test
    @DisplayName("피드 수정 성공 테스트")
    public void updateFeed_Ok() throws Exception{
        //given
        this.mockResDtoSetup();
        this.mockUserSetup();
        String contents = "contents update";
        FeedReqDto reqDto = new FeedReqDto(contents);
        MessageResDto<FeedResDto> messageResDto = new MessageResDto<>(HttpStatus.OK.value(), "게시물 수정이 완료되었습니다!", feedResDto);

        when(feedService.updateFeed(any(Long.class),any(FeedReqDto.class), any(User.class))).thenReturn(messageResDto);

        //when
        ResultActions resultActions = mockMvc.perform(put("/feeds/{feedId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqDto))
                .principal(mockPrincipal));

        //then
        resultActions
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("게시물 수정이 완료되었습니다!"))
                .andDo(print());

    }

    @Test
    @DisplayName("피드 수정 실패 테스트 - contents가 없는 경우")
    public void should_ThrowException_when_IfUpdatecontensIsMissing() throws Exception{
        //given
        this.mockResDtoSetup();
        this.mockUserSetup();
        String contents = "";
        FeedReqDto reqDto = new FeedReqDto(contents);

        //when
        ResultActions resultActions = mockMvc.perform(put("/feeds/{feedId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqDto))
                .principal(mockPrincipal));

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().string("contents should not be blank"))
                .andDo(print());

    }

    @Test
    @DisplayName("피드 삭제 성공 테스트")
    public void deleteFeed_Ok() throws Exception{
        //given
        this.mockResDtoSetup();
        this.mockUserSetup();
        MessageResDto<FeedResDto> messageResDto = new MessageResDto<>(HttpStatus.OK.value(), "게시물 삭제가 완료되었습니다!", feedResDto);

        when(feedService.deleteFeed(any(Long.class), any(User.class))).thenReturn(messageResDto);

        //when
        ResultActions resultActions = mockMvc.perform(delete("/feeds/{feedId}",1L)
                .principal(mockPrincipal));

        //then
        resultActions
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("게시물 삭제가 완료되었습니다!"))
                .andDo(print());

    }

    @Test
    @DisplayName("전체 피드 조회 성공 테스트")
    public void getAllFeeds_Ok() throws Exception {
        //given
        int page = 1;
        String sortBy = "createdAt";
        LocalDate startDate = null;
        LocalDate endDate = null;

        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.add("page", "1");
        requestParam.add("sortBy", "createdAt");

        this.mockResDtoSetup();
        List<FeedResDto> feedResList = new ArrayList<>();
        feedResList.add(feedResDto);
        MessageResDto<List<FeedResDto>> mockMessageResDto = new MessageResDto<>(200, "게시물 조회가 완료되었습니다!", feedResList);
//
//        FeedResDto mockFeedResDto = Mockito.mock(FeedResDto.class);
//        List<FeedResDto> mockList = Mockito.mock(List.class);
//        mockList.add(mockFeedResDto);
//        MessageResDto<List<FeedResDto>> mockMessageResDto = Mockito.mock(MessageResDto.class);
//
//        when(mockMessageResDto.getStatusCode()).thenReturn(200);
//        when(mockMessageResDto.getMessage()).thenReturn("완료");
//        when(mockMessageResDto.getData()).thenReturn(mockList);

        when(feedService.getAllFeeds(page - 1, sortBy, startDate, endDate)).thenReturn(mockMessageResDto);

        //when - then
        mockMvc.perform(get("/feeds/all").params(requestParam))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("게시물 조회가 완료되었습니다!"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andDo(print());
    }
    @Test
    @DisplayName("단건 피드 조회 성공 테스트")
    public void getFeed_Ok() throws Exception {
        //given
        this.mockResDtoSetup();
        MessageResDto<FeedResDto> mockMessageResDto = new MessageResDto<>(200, "게시물 조회가 완료되었습니다!", feedResDto);

        when(feedService.getFeed(1L)).thenReturn(mockMessageResDto);

        //when-then
        mockMvc.perform(get("/feeds/{feedId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("게시물 조회가 완료되었습니다!"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andDo(print());

    }

}