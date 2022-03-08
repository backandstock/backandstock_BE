package com.project.minibacktesting_be.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserInfoEditRequestDto {
    private String profileImgUrl = null;
}
