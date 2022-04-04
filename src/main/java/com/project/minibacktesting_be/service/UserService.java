package com.project.minibacktesting_be.service;

import com.project.minibacktesting_be.dto.user.SignupDto;
import com.project.minibacktesting_be.dto.user.UserInfoEditRequestDto;
import com.project.minibacktesting_be.exception.user.UserRegisterValidationException;
import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.model.User;
import com.project.minibacktesting_be.repository.PortfolioRepository;
import com.project.minibacktesting_be.repository.UserRepository;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import com.project.minibacktesting_be.security.vailidation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;


    // 회원가입
    public void registerUser(SignupDto signupDto){
        // 패스워드 암호화
        signupDto.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        // 회원가입 정보 유효성검사
        Validation.validationRegisterUser(signupDto, userRepository);

        // 회원가입시 이미지 저장 안함
        User user = new User.Builder(signupDto.getUsername(), signupDto.getNickname(), signupDto.getPassword())
                .build();
        userRepository.save(user);
    }

    // 유저정보 수정, 닉네임, 프로필 이미지 수정
    @Transactional
    public UserInfoEditRequestDto userInfoEdit(String nickname, MultipartFile multipartFile, UserDetailsImpl userDetails) throws IOException {
        User user = userDetails.getUser();
        UserInfoEditRequestDto userInfoEditRequestDto = new UserInfoEditRequestDto();

        // 닉네임 유효성 검사
        if (nickname.trim().isEmpty()) {
            throw new UserRegisterValidationException("User nickname is empty.");
            // 닉네임 중복확인
        } else if (userRepository.findByNickname(nickname).isPresent()) {
            if(!nickname.equals(userDetails.getUser().getNickname())){
                throw new UserRegisterValidationException( "Nickname : " + nickname + "is already exist.");
            }
        }

        if(multipartFile != null){
            // 기존 이미지가 있다면 S3서버에서 삭제
            if(user.getProfileImg() != null){
                s3Uploader.deleteFile(user.getProfileImg());
            }
            userInfoEditRequestDto.setProfileImgUrl(s3Uploader.upload(multipartFile, "images"));
            user.updateNicknameAndProfileImg(nickname, userInfoEditRequestDto.getProfileImgUrl());
        }

        user.updateNickname(nickname);

        userRepository.save(user);
        return userInfoEditRequestDto;
    }

    @Transactional
    public void userResign(UserDetailsImpl userDetails) throws UnsupportedEncodingException {
        if(userDetails.getUser().getProfileImg() != null){
            s3Uploader.deleteFile(userDetails.getUser().getProfileImg());
        }


        List<Portfolio> portfolios = portfolioRepository.findAllByUser(userDetails.getUser());
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        for(Portfolio portfolio:portfolios){
            vop.getOperations().delete("port"+portfolio.getId());
        }

        userRepository.delete(userDetails.getUser());
    }
}
