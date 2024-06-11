package com.sparta.newsfeedteamproject.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newsfeedteamproject.config.JwtConfig;
import com.sparta.newsfeedteamproject.dto.MessageResDto;
import com.sparta.newsfeedteamproject.dto.user.UserAuthReqDto;
import com.sparta.newsfeedteamproject.entity.Status;
import com.sparta.newsfeedteamproject.exception.ExceptionMessage;
import com.sparta.newsfeedteamproject.exception.FilterExceptionHandler;
import com.sparta.newsfeedteamproject.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthenticationFilter(JwtProvider jwtProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");

        try {
            UserAuthReqDto requestDto = new ObjectMapper().readValue(request.getInputStream(), UserAuthReqDto.class);
            //로그인 시도

            Status userStatus = (((UserDetailsImpl) userDetailsService.loadUserByUsername(requestDto.getUsername())).getUser()).getStatus();
            //유저 상태 확인 (탈퇴한 회원)
            if (Status.DEACTIVATE.equals(userStatus)) {
                log.error("탈퇴한 회원");
                throw new AccountStatusException(ExceptionMessage.DEATIVATE_USER.getExceptionMessage()) {
                    @Override
                    public String getMessage() {
                        return super.getMessage();
                    }
                };
            }

            //유저 상태 확인 (미 인증 회원)
            if (Status.UNAUTHORIZED.equals(userStatus)) {
                log.error("미 인증한 회원");
                throw new AccountStatusException(ExceptionMessage.UNAUTHORIZED_USER.getExceptionMessage()) {
                    @Override
                    public String getMessage() {
                        return super.getMessage();
                    }
                };
            }

            //정상 일 때 로그인 시도
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );

        } catch (IOException | AccountStatusException e) {
            log.error(e.getMessage());
            FilterExceptionHandler.handleExceptionInFilter(response, e);
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공");

        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        Status status = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getStatus();

        String accesstoken = jwtProvider.createAccessToken(username, status);
        String refreshtoken = jwtProvider.createRefreshToken(username, status);

        log.info("JWT 생성");

        response.addHeader(JwtConfig.ACCESS_TOKEN_HEADER, accesstoken);
        response.addHeader(JwtConfig.REFRESH_TOKEN_HEADER, refreshtoken);

        MessageResDto messageResDto = new MessageResDto(HttpStatus.NO_CONTENT.value(), "로그인 성공", null);
        ResponseEntity<MessageResDto> responseDto = new ResponseEntity<>(messageResDto, HttpStatus.OK);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseDto.getBody()));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401);
        FilterExceptionHandler.handleExceptionInFilter(response, failed);
    }
}