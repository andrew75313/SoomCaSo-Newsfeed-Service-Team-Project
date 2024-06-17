package com.sparta.newsfeedteamproject.service;


import com.sparta.newsfeedteamproject.dto.MessageResDto;
import com.sparta.newsfeedteamproject.dto.user.ProfileResDto;
import com.sparta.newsfeedteamproject.dto.user.SignupReqDto;
import com.sparta.newsfeedteamproject.dto.user.UpdateReqDto;
import com.sparta.newsfeedteamproject.dto.user.UserAuthReqDto;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.repository.UserRepository;
import com.sparta.newsfeedteamproject.security.UserDetailsImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 서버의 PORT 를 랜덤으로 설정합니다.
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 인스턴스의 생성 단위를 클래스로 변경합니다.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private UserDetailsImpl userDetails;
    private SignupReqDto signupReqDto;
    private User user;
    private UpdateReqDto updateReqDto;
    private UserAuthReqDto userAuthReqDto;

    private void signupReqDtoSetup() {
        String username = "ggumi12345";
        String password = "Ggumi1234567!";
        String name = "김꾸미";
        String email = "ggumi@gmail.com";
        String userInfo = "안녕하세요.";
        signupReqDto = new SignupReqDto(username, password, name, email, userInfo);
    }

    private void mockUserSetup() {
        user = userRepository.findById(1L).orElse(null);
        userDetails = new UserDetailsImpl(user);
    }

    private void updateReqDtoSetup() {
        String password = "Ggumi1234567!";
        String newPassword = "Ggumi12345678!";
        String newName = "김꾸밍";
        String newUserInfo = "안녕하세용";
        updateReqDto = new UpdateReqDto(password, newPassword, newName, newUserInfo);
    }

    private void userAuthReqDtoSetup() {
        String username = "ggumi12345";
        String password = "Ggumi12345678!";
        userAuthReqDto = new UserAuthReqDto(username, password);
    }

    @Test
    @DisplayName("회원 가입 테스트")
    public void signup_Ok() {
        //given
        this.signupReqDtoSetup();

        //when
        userService.signup(signupReqDto);
        MessageResDto responseDto = new MessageResDto(HttpStatus.OK.value(), "회원가입이 완료되었습니다!", null);

        //then
        assertNull(responseDto.getData());
        assertEquals(responseDto.getMessage(), "회원가입이 완료되었습니다!");
        assertEquals(responseDto.getStatusCode(), 200);
    }

    @Test
    @DisplayName("로그아웃 테스트")
    public void logout_Ok() {
        //given
        this.mockUserSetup();

        //when - then
        userService.logout(1L, userDetails);
    }

    @Test
    @DisplayName("프로필 조회")
    public void getProfile_Ok() {
        //given
        this.mockUserSetup();

        //when
        ProfileResDto profileResDto = userService.getProfile(1L);
        MessageResDto<ProfileResDto> responseDto = new MessageResDto<>(HttpStatus.OK.value(), "프로필 조회가 완료되었습니다!", profileResDto);

        //then
        assertNotNull(responseDto.getData());
        assertEquals(responseDto.getData().getName(), user.getName());
        assertEquals(responseDto.getData().getUserInfo(), user.getUserInfo());
        assertEquals(responseDto.getMessage(), "프로필 조회가 완료되었습니다!");
        assertEquals(responseDto.getStatusCode(), 200);
    }

    @Test
    @DisplayName("프로필 수정")
    public void editProfile_Ok() {
        //given
        this.mockUserSetup();
        this.updateReqDtoSetup();

        //when
        ProfileResDto profileResDto = userService.editProfile(1L, updateReqDto, userDetails);
        MessageResDto<ProfileResDto> responseDto = new MessageResDto<>(HttpStatus.OK.value(), "프로필 수정이 완료되었습니다!", profileResDto);

        //then
        assertNotNull(responseDto.getData());
        assertEquals(responseDto.getData().getName(), updateReqDto.getNewName());
        assertEquals(responseDto.getData().getUserInfo(), updateReqDto.getNewUserInfo());
        assertEquals(responseDto.getMessage(), "프로필 수정이 완료되었습니다!");
        assertEquals(responseDto.getStatusCode(), 200);
    }

    @Test
    @DisplayName("회원 탈퇴")
    public void withdraw_Ok() {
        //given
        this.mockUserSetup();
        this.userAuthReqDtoSetup();

        //when
        userService.withdraw(1L, userAuthReqDto, userDetails);
        MessageResDto responseDto = new MessageResDto(HttpStatus.OK.value(), "회원 탈퇴가 완료되었습니다!", null);

        //then
        assertNull(responseDto.getData());
        assertEquals(responseDto.getMessage(), "회원 탈퇴가 완료되었습니다!");
        assertEquals(responseDto.getStatusCode(), 200);
    }
}