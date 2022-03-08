package com.project.minibacktesting_be.model;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    // profileImg는 null 값 입력가능
    @Column
    private String profileImg;

    // 일반 회원가입인 경우 kakaoId는 null
    @Column(unique = true)
    private Long kakaoId;

    User(Builder builder) {
        this.username = builder.username;
        this.nickname = builder.nickname;
        this.password = builder.password;
        this.profileImg = builder.profileImg;
        this.kakaoId = builder.kakaoId;
    }

    public static class Builder {
        private String username;
        private String nickname;
        private String password;
        private String profileImg;
        private Long kakaoId;

        // 필수적인 필드들
        public Builder(String username, String nickname, String password) {
            this.username = username;
            this.nickname = nickname;
            this.password = password;
        }

        // 선택적인 필드들(null값 들어올수 있음)
        public Builder profileImg(String profileImg) {
            this.profileImg = profileImg;
            return this;
        }

        public Builder kakaoId(Long kakaoId) {
            this.kakaoId = kakaoId;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public void update(String nickname, String imgUrl) {
        this.nickname = nickname;
        this.profileImg = imgUrl;
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }
}
