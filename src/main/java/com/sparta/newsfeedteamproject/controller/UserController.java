package com.sparta.newsfeedteamproject.controller;

import com.sparta.newsfeedteamproject.dto.BaseResDto;
import com.sparta.newsfeedteamproject.dto.user.ProfileResDto;
import com.sparta.newsfeedteamproject.dto.user.SignupReqDto;
import com.sparta.newsfeedteamproject.dto.user.UpdateReqDto;
import com.sparta.newsfeedteamproject.dto.user.UserAuthReqDto;
import com.sparta.newsfeedteamproject.security.UserDetailsImpl;
import com.sparta.newsfeedteamproject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "user controller")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<BaseResDto> signup(@RequestBody @Valid SignupReqDto reqDto) {
        userService.signup(reqDto);
        BaseResDto responseDto = new BaseResDto(HttpStatus.OK.value(), "회원가입이 완료되었습니다!", null);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    @PutMapping("/status/{userId}")
    public ResponseEntity<BaseResDto> withdraw(@PathVariable Long userId, @RequestBody @Valid UserAuthReqDto reqDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.withdraw(userId, reqDto, userDetails);
        BaseResDto responseDto = new BaseResDto(HttpStatus.OK.value(), "회원 탈퇴가 완료되었습니다!", null);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResDto<Void>> login() {
        BaseResDto<Void> responseDto = new BaseResDto<>(HttpStatus.NO_CONTENT.value(), "로그인 성공!", null);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<Void> logout(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.logout(userId, userDetails);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<BaseResDto<ProfileResDto>> getProfile(@PathVariable Long userId) {
        BaseResDto<ProfileResDto> responseDto = new BaseResDto<>(HttpStatus.OK.value(), "프로필 조회가 완료되었습니다!", userService.getProfile(userId));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<BaseResDto<ProfileResDto>> editProfile(@PathVariable Long userId, @RequestBody @Valid UpdateReqDto reqDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BaseResDto<ProfileResDto> responseDto = new BaseResDto<>(HttpStatus.OK.value(), "프로필 수정이 완료되었습니다!", userService.editProfile(userId, reqDto, userDetails));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
