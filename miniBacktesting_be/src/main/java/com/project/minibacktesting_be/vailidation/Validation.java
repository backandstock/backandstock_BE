package com.project.minibacktesting_be.vailidation;

import com.project.minibacktesting_be.dto.MsgResponseDto;
import com.project.minibacktesting_be.dto.user.SignupDto;
import com.project.minibacktesting_be.repository.UserRepository;

import java.util.regex.Pattern;

public class Validation {
    public static void validationComment(String content) {
        // content 확인
        // 공백, null 입력 제한
        if (content.trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해 주세요.");
        }
    }

    public static MsgResponseDto validationRegisterUser(SignupDto signupDto, UserRepository repository) {
        // 이메일 공백입력 제한
        if (signupDto.getUsername().trim().isEmpty()) {
            return new MsgResponseDto("이메일을 입력해 주세요.");
            // 이메일 중복확인
        } else if (repository.findByUsername(signupDto.getUsername()).isPresent()) {
            return new MsgResponseDto("중복된 이메일입니다.");
            // 이메일 형식확인
            // 일반 최상위 도메인(gTLD)을 확인해보았는데, 일반적으로 3글자에서 가장 긴 .museum과 .travel이 6자로 가장길었다.
        } else if (!Pattern.matches("^[a-z0-9A-Z._-]+@[a-z0-9A-Z]+.[a-zA-Z]{3,6}$", signupDto.getUsername())) {
            return new MsgResponseDto("이메일 형식에 맞지않습니다.");
        }
        // 닉네임 공백입력 제한
        // 닉네임 중복검사
        return validationNickname(signupDto.getNickname(), repository);
    }

    public static MsgResponseDto validationNickname(String nickname, UserRepository repository){
        if (nickname.trim().isEmpty()) {
            return new MsgResponseDto("닉네임을 입력해 주세요.");
            // 닉네임 중복확인
        } else if (repository.findByNickname(nickname).isPresent()) {
            return new MsgResponseDto("중복된 닉네임입니다.");
        }
        return new MsgResponseDto("가입완료");
    }
}
