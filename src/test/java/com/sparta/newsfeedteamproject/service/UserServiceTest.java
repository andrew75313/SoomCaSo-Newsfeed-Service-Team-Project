package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.user.SignupReqDto;
import com.sparta.newsfeedteamproject.dto.user.UserAuthReqDto;
import com.sparta.newsfeedteamproject.entity.Status;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.exception.ExceptionMessage;
import com.sparta.newsfeedteamproject.repository.UserRepository;
import com.sparta.newsfeedteamproject.security.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    User user;

    private SignupReqDto signupReqDto;
    private User differentUser;
    private UserDetailsImpl userDetails;

    private void signupReqDtoSetup(){
        signupReqDto = Mockito.mock(SignupReqDto.class);

        when(signupReqDto.getUsername()).thenReturn("username");
        when(signupReqDto.getEmail()).thenReturn("user@gmail.com");
        when(signupReqDto.getPassword()).thenReturn("password");
        when(signupReqDto.getName()).thenReturn("name");
        when(signupReqDto.getUserInfo()).thenReturn("userInfo");
    }

    private void userSetup(){
        user = Mockito.mock(User.class);
        differentUser = Mockito.mock(User.class);
        userDetails = new UserDetailsImpl(user);

        when(user.getUsername()).thenReturn("username");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(differentUser));
    }

    @Test
    @DisplayName("회원 가입 테스트 - username이 중복 되었을 떄")
    public void should_ThrowException_when_DuplicateUserName(){
        //given
        this.signupReqDtoSetup();
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () ->{
            userService.signup(signupReqDto);
        });

        //then
        assertEquals(ExceptionMessage.DUPLICATE_USERNAME.getExceptionMessage(), exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    @DisplayName("회원 가입 테스트 - userEmail이 중복되었을 때")
    public void should_ThrowException_when_DuplicateUserEmail(){
        //given
        this.signupReqDtoSetup();
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () ->{
            userService.signup(signupReqDto);
        });

        //then
        assertEquals(ExceptionMessage.DUPLICATE_EMAIL.getExceptionMessage(), exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    @DisplayName("회원 가입 테스트")
    public void signUp_Ok(){
        //given
        this.signupReqDtoSetup();
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        //when
        userService.signup(signupReqDto);

        //then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals("username", capturedUser.getUsername());
        assertEquals("encodedPassword", capturedUser.getPassword());
        assertEquals("name", capturedUser.getName());
        assertEquals("user@gmail.com", capturedUser.getEmail());
        assertEquals("userInfo", capturedUser.getUserInfo());
        assertEquals(Status.UNAUTHORIZED, capturedUser.getStatus());
    }

    @Test
    @DisplayName("회원 탈퇴 테스트 - 로그인 한 유저의 username과 입력된 username이 일치하지 않을 때")
    public void should_ThrowException_when_UsernameIsNotCorrect(){
        //given
        this.userSetup();
        UserAuthReqDto reqDto = Mockito.mock(UserAuthReqDto.class);
        when(differentUser.getUsername()).thenReturn("username2");

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () ->{
            userService.withdraw(1L,reqDto,userDetails);
        });

        //then
        assertEquals(ExceptionMessage.INCORRECT_USER.getExceptionMessage(),exception.getMessage());
        verify(userRepository,never()).save(differentUser);

    }

    @Test
    @DisplayName("회원 탈퇴 테스트 - 로그인 한 유저의 비밀번호와 입력된 비밀번호가 일치하지 않을 때")
    public void should_ThrowException_when_PasswordIsNotCorrect(){
        //given
        this.userSetup();
        UserAuthReqDto reqDto = Mockito.mock(UserAuthReqDto.class);
        when(user.getPassword()).thenReturn("password");
        when(reqDto.getPassword()).thenReturn("password2");
        when(differentUser.getUsername()).thenReturn("username");

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () ->{
            userService.withdraw(1L,reqDto,userDetails);
        });

        //then
        assertEquals(ExceptionMessage.INCORRECT_PASSWORD.getExceptionMessage(),exception.getMessage());
        verify(userRepository,never()).save(differentUser);

    }

    @Test
    @DisplayName("회원 탈퇴 테스트 - 이미 회원탈퇴한 회원일 때")
    public void should_ThrowException_when_UserStatusIsDEACTIVATE(){
        //given
        this.userSetup();
        UserAuthReqDto reqDto = Mockito.mock(UserAuthReqDto.class);
        when(differentUser.getUsername()).thenReturn("username");

        when(user.getPassword()).thenReturn("password");
        when(reqDto.getPassword()).thenReturn("password");

        when(passwordEncoder.matches(any(String.class),any(String.class))).thenReturn(true);
        when(differentUser.getStatus()).thenReturn(Status.DEACTIVATE);

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () ->{
            userService.withdraw(1L,reqDto,userDetails);
        });

        //then
        assertEquals(ExceptionMessage.DEATIVATE_USER.getExceptionMessage(),exception.getMessage());
        verify(userRepository,never()).save(differentUser);
    }
}