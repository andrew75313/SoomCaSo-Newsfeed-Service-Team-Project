package com.sparta.newsfeedteamproject.controller;

import com.sparta.newsfeedteamproject.dto.BaseResDto;
import com.sparta.newsfeedteamproject.security.UserDetailsImpl;
import com.sparta.newsfeedteamproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/logout")
    public ResponseEntity<BaseResDto> logout(UserDetailsImpl userDetails) {
        userService.logout(userDetails);
        BaseResDto resDto = new BaseResDto(HttpStatus.NO_CONTENT.value(), "로그아웃이 완료되었습니다");
        return new ResponseEntity<>(resDto, HttpStatus.NO_CONTENT);
    }
}
