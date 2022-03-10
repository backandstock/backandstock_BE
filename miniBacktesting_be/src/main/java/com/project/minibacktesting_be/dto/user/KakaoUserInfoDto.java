package com.project.minibacktesting_be.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String nickname;
    private String thumbnailUrl;

    public KakaoUserInfoDto(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
