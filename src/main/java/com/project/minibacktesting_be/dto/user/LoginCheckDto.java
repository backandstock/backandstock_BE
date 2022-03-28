package com.project.minibacktesting_be.dto.user;

import com.project.minibacktesting_be.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginCheckDto {
    private Long userId;
    private String nickname;
    private String profileImg;

    public LoginCheckDto(User user){
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.profileImg = user.getProfileImg();
    }
}
