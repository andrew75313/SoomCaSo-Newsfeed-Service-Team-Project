package com.sparta.newsfeedteamproject.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserAuthReqDto {
    @NotBlank(message = "[username:blank] 사용자 아이디를 작성해주세요!")
    @Size(min = 10, max = 20, message = "[username:size] 10자 이상 20자 이하로 작성해주세요!")
    @Pattern(regexp = "^[0-9a-zA-Z]+$", message = "[username:pattern] 숫자와 영문 대소문자를 포함하여 작성해주세요!")
    private String username;
    @NotBlank(message = "[password:blank] 비밀번호를 작성해주세요!")
    @Size(min = 10, message = "[password:size] 10자 이상으로 작성해주세요!")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{10,}$", message = "[password:pattern] 숫자, 영문 대소문자, 특수기호를 최소 한개씩 포함하여 작성해주세요!")
    private String password;
}