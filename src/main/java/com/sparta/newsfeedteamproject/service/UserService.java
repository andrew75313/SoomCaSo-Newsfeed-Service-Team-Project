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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupReqDto reqDto) {

        String username = reqDto.getUsername();
        String password = passwordEncoder.encode(reqDto.getPassword());
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

        if(!passwordEncoder.matches(reqDto.getPassword(),password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않아 회원탈퇴가 불가능합니다.");
        }

        if(userDetails.getUser().getStatus() == Status.DEACTIVATE){
            throw new IllegalArgumentException("이미 탈퇴된 사용자는 재탈퇴가 불가능합니다.");
        }

        User user = userDetails.getUser();
        user.setStatus(Status.DEACTIVATE);
        user.setStatusModTime(LocalDateTime.now());

        userRepository.save(user);
    }

    @Transactional
    public void logout(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
        );
        user.deleteRefreshToken();
    }

    public ProfileResDto getProfile(Long userId) {
        User checkUser = userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
        );
        if(checkUser.getStatus().equals(Status.DEACTIVATE)){
            throw new IllegalArgumentException("탈퇴한 사용자는 프로필 조회가 불가능합니다.");
        }
        return new ProfileResDto(checkUser);
    }

    public ProfileResDto editProfile(Long userId, UpdateReqDto reqDto, UserDetailsImpl userDetails) {

        String username = userDetails.getUser().getUsername();

        User loginUser = findByUsername(username);
        User checkUser = findById(userId);

        if(!loginUser.getUsername().equals(checkUser.getUsername())){
            throw new IllegalArgumentException("프로필 사용자가 일치하지 않아 수정이 불가능합니다.");
        }

        if(checkUser.getStatus().equals(Status.DEACTIVATE)){
            throw new IllegalArgumentException("탈퇴한 사용자는 프로필 수정이 불가능합니다.");
        }

        String password = userDetails.getUser().getPassword();

        if(!passwordEncoder.matches(reqDto.getPassword(),password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않아 프로필 수정이 불가능합니다.");
        }

        if(reqDto.getNewPassword().equals(reqDto.getPassword())){
            throw new IllegalArgumentException("기존 비밀번호와 일치하여 수정이 불가능합니다.");
        }

        String name = reqDto.getNewName();
        String userInfo = reqDto.getNewUserInfo();
        String newPassword = passwordEncoder.encode(reqDto.getNewPassword());
        LocalDateTime modifiedAt = LocalDateTime.now();

        checkUser.update(name,userInfo,newPassword,modifiedAt);
        userRepository.save(checkUser);

        return new ProfileResDto(checkUser);
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );
    }
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );
    }
}
