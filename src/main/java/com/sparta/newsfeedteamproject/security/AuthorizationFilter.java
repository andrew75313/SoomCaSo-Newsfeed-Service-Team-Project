package com.sparta.newsfeedteamproject.security;

import com.sparta.newsfeedteamproject.config.JwtConfig;
import com.sparta.newsfeedteamproject.exception.ExceptionMessage;
import com.sparta.newsfeedteamproject.exception.FilterExceptionHandler;
import com.sparta.newsfeedteamproject.util.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j(topic = "JWT 검증 및 인가")
public class AuthorizationFilter extends OncePerRequestFilter {
    private final String[] whiteList = {"/users/signup", "/users/login", "/feeds/all", "/users/signup/**"};
    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthorizationFilter(JwtProvider jwtProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        if (!whiteListCheck(req.getRequestURI()) && !"GET".equals(req.getMethod())) {

            String raw_accessTokenValue = jwtProvider.getJwtFromHeader(req, JwtConfig.ACCESS_TOKEN_HEADER);
            String raw_refreshTokenValue = jwtProvider.getJwtFromHeader(req, JwtConfig.REFRESH_TOKEN_HEADER);
            try {
                if (StringUtils.hasText(raw_accessTokenValue) && StringUtils.hasText(raw_refreshTokenValue)) {
                    // JWT 토큰 substring
                    String accessTokenValue = jwtProvider.substringToken(raw_accessTokenValue);
                    String refreshTokenValue = jwtProvider.substringToken(raw_refreshTokenValue);

                    //둘 다 유효하지 않을 때
                    if (!jwtProvider.isTokenValidate(accessTokenValue) && !jwtProvider.isTokenValidate(refreshTokenValue)) {
                        throw new IllegalArgumentException(ExceptionMessage.UNVALID_TOKEN.getExceptionMessage());
                    }

                    Claims info = jwtProvider.getUserInfoFromToken(refreshTokenValue);

                    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetailsService.loadUserByUsername(info.getSubject());

                    //DB의 refreshtoken과 같은지 비교 (조작된 토큰인지 확인)
                    if (!(Objects.equals(refreshTokenValue, jwtProvider.substringToken((userDetailsImpl.getUser().getRefreshToken()))))) {
                        throw new IllegalArgumentException(ExceptionMessage.UNVALID_TOKEN.getExceptionMessage());
                    }

                    //로그아웃 요청일 땐 Header에 토큰 추가 X
                    if (!req.getRequestURI().matches("/users/logout/\\d+")) {
                        if ((!jwtProvider.isTokenValidate(accessTokenValue) && jwtProvider.isTokenValidate(refreshTokenValue))) { //refresh만 정상일 때

                            //토큰 재생성
                            jwtProvider.reCreateTokens(info.getSubject(), res);
                            log.info("토큰 재생성 완료");
                        } else { //두 토큰 다 정상일 때
                            res.addHeader(JwtConfig.ACCESS_TOKEN_HEADER, raw_accessTokenValue);
                            res.addHeader(JwtConfig.REFRESH_TOKEN_HEADER, raw_refreshTokenValue);
                        }
                    }

                    setAuthentication(info.getSubject());


                } else {
                    throw new IllegalArgumentException("해당 기능을 사용하기 위해선 로그인 해야 합니다.");
                }
            } catch (IllegalArgumentException e) {
                log.error(e.getMessage());
                FilterExceptionHandler.handleExceptionInFilter(res, e);
                return;
            } catch (ExpiredJwtException e) {
                log.error(e.getMessage());
                FilterExceptionHandler.handleJwtExceptionInFilter(res, ExceptionMessage.EXPIRATION_TOKEN);
                return;
            } catch (Exception e) {
                log.error(e.getMessage());
                FilterExceptionHandler.handleExceptionInFilter(res, e);
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

    private boolean whiteListCheck(String uri) {
        return PatternMatchUtils.simpleMatch(whiteList, uri);
    }
}
