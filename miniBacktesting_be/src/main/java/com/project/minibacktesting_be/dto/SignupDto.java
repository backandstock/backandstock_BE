package com.project.minibacktesting_be.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SignupDto {
    private String username;
    private String password;
    private String nickname;
    private String profileImg;
}
