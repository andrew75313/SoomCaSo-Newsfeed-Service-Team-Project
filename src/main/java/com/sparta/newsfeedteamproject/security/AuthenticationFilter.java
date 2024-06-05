package com.sparta.newsfeedteamproject.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newsfeedteamproject.dto.user.UserAuthReqDto;
import com.sparta.newsfeedteamproject.entity.Status;
import com.sparta.newsfeedteamproject.exception.FilterExceptionHandler;
import com.sparta.newsfeedteamproject.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtProvider jwtProvider;

    public AuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");

        try {
            UserAuthReqDto requestDto = new ObjectMapper().readValue(request.getInputStream(), UserAuthReqDto.class);
            //로그인 시도
            Authentication auth = getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );

            //유저 상태 확인
            if(!"ACTIVATE".equals(((UserDetailsImpl) auth.getPrincipal()).getUser().getStatus())){
                log.error("탈퇴한 회원");
                throw new AccountStatusException("탈퇴한 회원입니다.") {
                    @Override
                    public String getMessage() {
                        return super.getMessage();
                    }
                };
            }

            return auth;
        } catch (IOException | AccountStatusException e) {
            log.error(e.getMessage());
            FilterExceptionHandler.handleExceptionInFilter(response,e);
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

        response.addHeader(JwtProvider.ACCESS_TOKEN_HEADER, accesstoken);
        response.addHeader(JwtProvider.REFRESH_TOKEN_HEADER, refreshtoken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401);
    }
}