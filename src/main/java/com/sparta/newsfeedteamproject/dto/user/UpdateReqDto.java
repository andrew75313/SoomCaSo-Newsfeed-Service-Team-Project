package com.sparta.newsfeedteamproject.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateReqDto {
    @NotBlank(message = "[password:blank] 비밀번호를 작성해주세요!")
    @Size(min = 10, message = "[password:size] 10자 이상으로 작성해주세요!")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{10,}$", message = "[password:pattern] 숫자, 영문 대소문자, 특수기호를 최소 한개씩 포함하여 작성해주세요!")
    private String password;
    @NotBlank(message = "[newPassword:blank] 비밀번호를 작성해주세요!")
    @Size(min = 10, message = "[newPassword:size] 10자 이상으로 작성해주세요!")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{10,}$", message = "[newPassword:pattern] 숫자, 영문 대소문자, 특수기호를 최소 한개씩 포함하여 작성해주세요!")
    private String newPassword;
    @NotBlank(message = "[newName:blank] 사용자 이름을 작성해주세요!")
    private String newName;
    private String newUserInfo;
}