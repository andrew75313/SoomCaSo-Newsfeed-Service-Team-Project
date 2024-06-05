package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.user.SignupReqDto;
import com.sparta.newsfeedteamproject.entity.Status;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signup(SignupReqDto reqDto) {

        String loginId = reqDto.getLoginId();
        String password = bCryptPasswordEncoder.encode(reqDto.getPassword());
        String name = reqDto.getName();
        String email = reqDto.getEmail();
        String userInfo = reqDto.getUserInfo();

        log.info("중복 아이디 확인");
        Optional<User> checkLoginId = userRepository.findByLoginId(loginId);
        if (checkLoginId.isPresent()) {
            throw new IllegalArgumentException("중복된 로그인 아이디 입니다.");
        }

        log.info("중복 이메일 확인");
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 이메일입니다.");
        }

        Status status = Status.ACTIVATE;
        LocalDateTime statusModTime = LocalDateTime.now();

        log.info("새로운 사용자 등록");
        User user = new User(loginId,password,name,email,userInfo,status,statusModTime);
        userRepository.save(user);
    }
}
