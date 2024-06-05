package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.user.ProfileResDto;
import com.sparta.newsfeedteamproject.dto.user.SignupReqDto;
import com.sparta.newsfeedteamproject.dto.user.UpdateReqDto;
import com.sparta.newsfeedteamproject.dto.user.UserAuthReqDto;
import com.sparta.newsfeedteamproject.entity.Status;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.repository.UserRepository;
import com.sparta.newsfeedteamproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signup(SignupReqDto reqDto) {

        String username = reqDto.getUsername();
        String password = bCryptPasswordEncoder.encode(reqDto.getPassword());
        String name = reqDto.getName();
        String email = reqDto.getEmail();
        String userInfo = reqDto.getUserInfo();

        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 이름 입니다.");
        }

        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 이메일입니다.");
        }

        Status status = Status.ACTIVATE;
        LocalDateTime statusModTime = LocalDateTime.now();

        User user = new User(username,password,name,email,userInfo,status,statusModTime);
        userRepository.save(user);
    }

    public void withdraw(UserAuthReqDto reqDto, UserDetailsImpl userDetails) {

        String password = userDetails.getUser().getPassword();

        if(!reqDto.getPassword().equals(password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않아 회원탈퇴가 불가능합니다.");
        }

        if(userDetails.getUser().getStatus() == Status.DEACTIVATE){
            throw new IllegalArgumentException("이미 탈퇴된 사용자는 재탈퇴가 불가능합니다.");
        }

        userDetails.getUser().setStatus(Status.DEACTIVATE);
        userRepository.save(userDetails.getUser());
    }

    @Transactional
    public void logout(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        user.deleteRefreshToken();
    }

    public ProfileResDto getProfile(Long userId) {
        User checkUser = userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
        );
        return new ProfileResDto(checkUser);
    }

    public ProfileResDto editProfile(UpdateReqDto reqDto, UserDetailsImpl userDetails) {

        String username = userDetails.getUser().getUsername();

        User checkUser = userRepository.findByUsername(username).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        String password = userDetails.getUser().getPassword();

        if(!bCryptPasswordEncoder.matches(reqDto.getPassword(),password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않아 프로필 수정이 불가능합니다.");
        }

        if(reqDto.getNewPassword().equals(password)){
            throw new IllegalArgumentException("기존 비밀번호와 같아 수정이 불가능합니다.");
        }

        String name = reqDto.getName();
        String userInfo = reqDto.getUserInfo();
        String newPassword = bCryptPasswordEncoder.encode(reqDto.getNewPassword());

        checkUser.update(name,userInfo,newPassword);
        userRepository.save(checkUser);

        return new ProfileResDto(checkUser);
    }
}
