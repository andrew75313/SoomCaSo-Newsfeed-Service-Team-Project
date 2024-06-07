package com.sparta.newsfeedteamproject.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateReqDto {
    @NotBlank
    @Size(min = 10)
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{10,}$")
    private String password;
    @NotBlank
    @Size(min = 10)
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{10,}$")
    private String newPassword;
    @NotBlank
    private String newName;
    private String newUserInfo;
}
