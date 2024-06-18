package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.MessageResDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedReqDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedResDto;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.repository.FeedRepository;
import com.sparta.newsfeedteamproject.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 서버의 PORT 를 랜덤으로 설정합니다.
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 인스턴스의 생성 단위를 클래스로 변경합니다.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class FeedServiceIntegrationTest {

    @Autowired
    FeedService feedService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FeedRepository feedRepository;

    User user;
    Long feedId;
    FeedResDto createdFeed = null;

    private void UserSetup(){
        user = userRepository.findById(1L).orElse(null);
    }

    @Test
    @Order(1)
    @DisplayName("피드 생성 테스트")
    public void createFeed_Ok(){
        //given
        this.UserSetup();
        String contents = "contents";
        FeedReqDto feedReqDto = new FeedReqDto(contents);

        //when
        MessageResDto<FeedResDto> response = feedService.createFeed(feedReqDto,user);
        FeedResDto feedResDto = response.getData();

        //then
        assertNotNull(feedResDto.getId());
        assertEquals(feedResDto.getContents(),contents);
        createdFeed = feedResDto;
        feedId = feedResDto.getId();
    }

    @Test
    @Order(2)
    @DisplayName("피드 수정 테스트")
    public void updateFeed_Ok(){
        //given
        this.UserSetup();
        String newContents = "new contents";
        FeedReqDto feedReqDto = new FeedReqDto(newContents);


        //when
        MessageResDto<FeedResDto> response = feedService.updateFeed(feedId,feedReqDto,user);
        FeedResDto feedResDto = response.getData();

        //then
        assertNotNull(feedResDto.getId());
        assertEquals(feedResDto.getContents(),newContents);
        createdFeed = feedResDto;
    }

    @Test
    @Order(3)
    @DisplayName("피드 조회 테스트")
    public void getFeed_Ok(){
        //given

        //when
        MessageResDto<FeedResDto> response = feedService.getFeed(feedId);
        FeedResDto feedResDto = response.getData();

        //then
        assertNotNull(feedResDto.getId());
        assertNotNull(feedResDto.getContents());
    }

    @Test
    @Order(4)
    @DisplayName("모든 피드 조회 테스트 - default : 1page, createdAt 순 정렬")
    public void getAllFeeds_Ok() {

        //given
        int page = 1;
        String sortBy = "createdAt";
        LocalDate startDate = null;
        LocalDate endDate = null;

        //when
        MessageResDto<List<FeedResDto>> response = feedService.getAllFeeds(page - 1, sortBy, startDate, endDate);
        List<FeedResDto> feedResDtoList = response.getData();

        //then
        assertNotNull(feedResDtoList.get(0).getId());
    }

    @Test
    @Order(5)
    @DisplayName("피드 삭제 테스트")
    public void deleteFeeds_Ok(){
        //given
        this.UserSetup();

        //when
        MessageResDto<FeedResDto> response = feedService.deleteFeed(feedId,user);

        //then
        assertNull(response.getData());
    }
}
