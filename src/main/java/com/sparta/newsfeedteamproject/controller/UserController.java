package com.sparta.newsfeedteamproject.controller;

import com.sparta.newsfeedteamproject.dto.BaseResDto;
import com.sparta.newsfeedteamproject.dto.user.ProfileResDto;
import com.sparta.newsfeedteamproject.dto.user.SignupReqDto;
import com.sparta.newsfeedteamproject.dto.user.UpdateReqDto;
import com.sparta.newsfeedteamproject.dto.user.UserAuthReqDto;
import com.sparta.newsfeedteamproject.jwt.JwtProvider;
import com.sparta.newsfeedteamproject.security.UserDetailsImpl;
import com.sparta.newsfeedteamproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;


    @PostMapping("/signup")
    public ResponseEntity<BaseResDto> signup(@RequestBody @Valid SignupReqDto reqDto) {
        userService.signup(reqDto);
        BaseResDto responseDto = new BaseResDto(HttpStatus.OK.value(), "회원가입이 완료되었습니다!",null);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    @PutMapping("/status")
    public ResponseEntity<BaseResDto> withdraw(@RequestBody @Valid UserAuthReqDto reqDto,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.withdraw(reqDto,userDetails);
        BaseResDto responseDto = new BaseResDto(HttpStatus.OK.value(), "회원 탈퇴가 완료되었습니다!",null);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResDto> logout(HttpServletRequest request) {
        String token = jwtProvider.substringToken(jwtProvider.getJwtFromHeader(request, JwtProvider.ACCESS_TOKEN_HEADER));
        String username = jwtProvider.getUserInfoFromToken(token).getSubject();
        userService.logout(username);
        BaseResDto resDto = new BaseResDto(HttpStatus.NO_CONTENT.value(), "로그아웃이 완료되었습니다",null);
        return new ResponseEntity<>(resDto, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<BaseResDto<ProfileResDto>> getProfile(@PathVariable Long userId) {
        BaseResDto<ProfileResDto> responseDto = new BaseResDto<>(HttpStatus.OK.value(), "프로필 조회가 완료되었습니다!",userService.getProfile(userId));
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<BaseResDto<ProfileResDto>> editProfile(@PathVariable Long userId,@RequestBody @Valid UpdateReqDto reqDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BaseResDto<ProfileResDto> responseDto = new BaseResDto<>(HttpStatus.OK.value(), "프로필 수정이 완료되었습니다!",userService.editProfile(userId,reqDto,userDetails));
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

}
