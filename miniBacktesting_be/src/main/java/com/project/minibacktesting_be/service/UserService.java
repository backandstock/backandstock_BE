package com.project.minibacktesting_be.service;

import com.project.minibacktesting_be.dto.user.SignupDto;
import com.project.minibacktesting_be.dto.user.UserInfoEditRequestDto;
import com.project.minibacktesting_be.dto.user.UserResignResponseDto;
import com.project.minibacktesting_be.model.User;
import com.project.minibacktesting_be.repository.UserRepository;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    //회원가입 시 이메일 중복 및 아이디 검사, 비밀번호 암호화
    public void registerUser(SignupDto signupDto){
        // 패스워드 암호화
        signupDto.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        checkUsername(signupDto.getUsername());

        // 회원가입서 이미지 저장 안함
        User user = new User.Builder(signupDto.getUsername(), signupDto.getNickname(), signupDto.getPassword())
                .build();
        userRepository.save(user);
    }

    // 유저네임 중복 확인 함수
    public void checkUsername(String username) {
        Optional<User> foundUsername = userRepository.findByUsername(username);
        if (foundUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 유저네임 입니다.");
        }
    }

    // 유저정보 수정, 닉네임, 프로필 이미지 수정
    @Transactional
    public UserInfoEditRequestDto userInfoEdit(String nickname, MultipartFile multipartFile, UserDetailsImpl userDetails) throws IOException {
        User user = userDetails.getUser();
        UserInfoEditRequestDto userInfoEditRequestDto = new UserInfoEditRequestDto();

        if(multipartFile != null && !nickname.trim().isEmpty()){
            // 기존 img가 있다면 S3서버에서 삭제
            if(!user.getProfileImg().trim().isEmpty()){
                s3Uploader.deleteFile(user.getProfileImg());
            }
            userInfoEditRequestDto.setProfileImgUrl(s3Uploader.upload(multipartFile, "images"));
            user.update(nickname, userInfoEditRequestDto.getProfileImgUrl());
            userRepository.save(user);
        }else if(nickname.trim().isEmpty()){
            throw new IllegalArgumentException("변경할 닉네임을 입력해 주세요.");
        }
        user.update(nickname);
        userRepository.save(user);
        return userInfoEditRequestDto;
    }

    @Transactional
    public UserResignResponseDto userResign(UserDetailsImpl userDetails) throws UnsupportedEncodingException {
        if(userDetails.getUser().getProfileImg() != null){
            s3Uploader.deleteFile(userDetails.getUser().getProfileImg());
        }
        userRepository.delete(userDetails.getUser());
        UserResignResponseDto responseDto = new UserResignResponseDto();
        responseDto.setResult(true);
        return responseDto;
    }
}
