package com.project.minibacktesting_be.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User {
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    User(Builder builder){
        this.username = builder.username;
        this.nickname = builder.nickname;
        this.password = builder.password;
        this.profileImg = builder.profileImg;
    }

    public static class Builder{
        private String username;
        private String nickname;
        private String password;
        private String profileImg;

        // 필수적인 필드들
        public Builder(String username, String nickname, String password){
            this.username = username;
            this.nickname = nickname;
            this.password = password;
        }

        // 선택적인 필드들(null값 들어올수 있음)
        public Builder profileImg(String profileImg){
            this.profileImg= profileImg;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}
