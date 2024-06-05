package com.sparta.newsfeedteamproject.dto.user;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class SignupReqDto {

    @NotBlank
    @Size(min = 10, max = 20)
    @Pattern(regexp = "^[0-9a-zA-Z]+$")
    private String username;
    @NotBlank
    @Size(min = 10)
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{10,}$")
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    private String userInfo;
}
