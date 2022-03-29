package com.project.minibacktesting_be.vailidation;

import com.project.minibacktesting_be.dto.user.SignupDto;
import com.project.minibacktesting_be.exception.comment.CommentValidationException;
import com.project.minibacktesting_be.exception.user.UserMatchException;
import com.project.minibacktesting_be.exception.user.UserRegisterValidationException;
import com.project.minibacktesting_be.repository.UserRepository;

import java.util.regex.Pattern;

public class Validation {
    public static void validationComment(String content) {
        // content 확인
        // 공백, null 입력 제한

        if (content.trim().isEmpty()) {
            throw new CommentValidationException("Comment contents is empty.");
        }
    }

    public static void validationRegisterUser(SignupDto signupDto, UserRepository repository) {
        // 이메일 공백입력 제한
        if (signupDto.getUsername().trim().isEmpty()) {
            throw new UserRegisterValidationException("User email is empty.");
            // 이메일 중복확인
        } else if (repository.findByUsername(signupDto.getUsername()).isPresent()) {
            throw new UserRegisterValidationException( "User email : "+ signupDto.getUsername() + " is already exist.");
            // 이메일 형식확인
            // 일반 최상위 도메인(gTLD)을 확인해보았는데, 일반적으로 3글자에서 가장 긴 .museum과 .travel이 6자로 가장길었다.
        } else if (!Pattern.matches("^[a-z0-9A-Z._-]+@[a-z0-9A-Z]+.[a-zA-Z]{3,6}$", signupDto.getUsername())) {
            throw new UserRegisterValidationException("User email format doesn't match. Input email : " + signupDto.getUsername());
        }
        // 닉네임 공백입력 제한
        // 닉네임 중복검사
        validationNickname(signupDto.getNickname(), repository);
    }

    public static void validationNickname(String nickname, UserRepository repository){
        if (nickname.trim().isEmpty()) {
            throw new UserRegisterValidationException("User nickname is empty.");
            // 닉네임 중복확인
        } else if (repository.findByNickname(nickname).isPresent()) {
            throw new UserRegisterValidationException( "Nickname : " + nickname + "is already exist.");
        }
    }
}
