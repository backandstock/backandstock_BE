package com.project.minibacktesting_be.service;

import com.project.minibacktesting_be.dto.SignupDto;
import com.project.minibacktesting_be.model.User;
import com.project.minibacktesting_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입 시 이메일 중복 및 아이디 검사, 비밀번호 암호화
    public void registerUser(SignupDto signupDto){
        //패스워드 암호화
        signupDto.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        checkUsername(signupDto.getUsername());
        User user = new User.Builder(signupDto.getUsername(), signupDto.getNickname(), signupDto.getPassword())
                .profileImg(signupDto.getProfileImg())
                .build();
        userRepository.save(user);
    }

    // 유저네임 중복 확인 함수
    public void checkUsername(String username){
        Optional<User> foundUsername = userRepository.findByUsername(username);
        if (foundUsername.isPresent()){
            throw new IllegalArgumentException("중복된 유저네임 입니다.");
        }
    }
}
