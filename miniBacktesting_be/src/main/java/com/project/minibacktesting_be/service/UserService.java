package com.project.minibacktesting_be.service;

import com.project.minibacktesting_be.dto.MsgResponseDto;
import com.project.minibacktesting_be.dto.user.SignupDto;
import com.project.minibacktesting_be.dto.user.UserInfoEditRequestDto;
import com.project.minibacktesting_be.model.User;
import com.project.minibacktesting_be.repository.UserRepository;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import com.project.minibacktesting_be.vailidation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    // 회원가입
    public MsgResponseDto registerUser(SignupDto signupDto){
        // 패스워드 암호화
        signupDto.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        // 회원가입 정보 유효성검사
        MsgResponseDto msgResponseDto = Validation.validationRegisterUser(signupDto, userRepository);
        if(!msgResponseDto.getMsg().equals("가입완료")){
            return msgResponseDto;
        }

        // 회원가입시 이미지 저장 안함
        User user = new User.Builder(signupDto.getUsername(), signupDto.getNickname(), signupDto.getPassword())
                .build();
        userRepository.save(user);
        return msgResponseDto;
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
        }
        // 닉네임 유효성 검사
        String valadationResult = Validation.validationNickname(nickname, userRepository).getMsg();
        if(!valadationResult.equals("가입완료")){
            throw new IllegalArgumentException(valadationResult);
        }

        user.update(nickname);
        userRepository.save(user);
        return userInfoEditRequestDto;
    }

    @Transactional
    public void userResign(UserDetailsImpl userDetails) throws UnsupportedEncodingException {
        if(userDetails.getUser().getProfileImg() != null){
            s3Uploader.deleteFile(userDetails.getUser().getProfileImg());
        }
        userRepository.delete(userDetails.getUser());
    }
}
