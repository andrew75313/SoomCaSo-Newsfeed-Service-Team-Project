package com.sparta.newsfeedteamproject.security;

import com.sparta.newsfeedteamproject.exception.FilterExceptionHandler;
import com.sparta.newsfeedteamproject.jwt.JwtProvider;
import com.sparta.springauth.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserDetailsImpl userDetails;

    public AuthorizationFilter(JwtProvider jwtProvider, UserDetailsServiceImpl userDetailsService, UserDetailsImpl userDetails) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.userDetails = userDetails;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String accessTokenValue = jwtProvider.getJwtFromHeader(req,JwtProvider.ACCESS_TOKEN_HEADER);
        String refreshTokenValue = jwtProvider.getJwtFromHeader(req,JwtProvider.REFRESH_TOKEN_HEADER);

        if (StringUtils.hasText(accessTokenValue) && StringUtils.hasText(refreshTokenValue)) {
            // JWT 토큰 substring
            accessTokenValue = jwtProvider.substringToken(accessTokenValue);
            refreshTokenValue = jwtProvider.substringToken(refreshTokenValue);

            log.info(accessTokenValue);
            log.info(refreshTokenValue);

            try {
                if (!jwtProvider.isTokenValidate(accessTokenValue) && !jwtProvider.isTokenValidate(refreshTokenValue)) {
                    throw new ValidationException("유효하지 않은 토큰입니다. 다시 로그인해주세요.");
                }

                if (!(refreshTokenValue == userDetails.getUser().getRefreshToken())) {
                    throw new ValidationException("유효하지 않은 토큰입니다. 다시 로그인해주세요.");
                }
            } catch(ValidationException e){
                log.error(e.getMessage());
                FilterExceptionHandler.handleExceptionInFilter(res,e);
                return;
            }

            Claims info = jwtProvider.getUserInfoFromToken(accessTokenValue);

            //토큰 재생성
            jwtProvider.reCreateTokens(info.getSubject(),res);

            log.info("토큰 재생성 완료");

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
