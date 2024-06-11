package com.sparta.newsfeedteamproject.config;

import com.sparta.newsfeedteamproject.security.AuthenticationFilter;
import com.sparta.newsfeedteamproject.security.AuthorizationFilter;
import com.sparta.newsfeedteamproject.security.UserDetailsServiceImpl;
import com.sparta.newsfeedteamproject.util.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(JwtProvider jwtProvider, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtProvider = jwtProvider;

        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter filter = new AuthenticationFilter(jwtProvider, userDetailsService);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public AuthorizationFilter authorizationFilter() {
        return new AuthorizationFilter(jwtProvider, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.sessionManagement(
                (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests(
                (authorizationHttpRequests) -> authorizationHttpRequests
                        .requestMatchers(HttpMethod.POST, "/users/signup").permitAll()
                        .requestMatchers("/users/signup/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/profile/{userId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/feeds/{feedId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/feeds/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/feeds/{feedId}/comments/{commentId}").permitAll()
                        .anyRequest().authenticated()

        );

        http.formLogin(AbstractHttpConfigurer::disable);

        http.addFilterBefore(authorizationFilter(), AuthenticationFilter.class);
        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}