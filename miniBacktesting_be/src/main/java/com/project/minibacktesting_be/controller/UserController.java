package com.project.minibacktesting_be.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.minibacktesting_be.dto.user.*;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import com.project.minibacktesting_be.service.KakaoUserService;
import com.project.minibacktesting_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    // 회원가입
    @PostMapping("/users")
    public void registerUser(@RequestBody SignupDto requestDto) {
        userService.registerUser(requestDto);
    }

    // 로그인 정보
    @PostMapping("/users/info")
    public LoginCheckDto userLoginCheck(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return new LoginCheckDto(userDetails.getUser());
    }

    // 회원정보 수정
    @PutMapping("/users")
    public UserInfoEditRequestDto userInfoEdit(@RequestParam("nickname") String nickname,
                                               @RequestParam(value = "profileImage", required = false) MultipartFile multipartFile,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return userService.userInfoEdit(nickname, multipartFile, userDetails);
    }

    // 카카오 로그인
    @GetMapping("/user/kakao/callback")
    public KakaoUserInfoDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoUserService.kakaoLogin(code, response);
    }

    // 회원탈퇴
    @DeleteMapping("/users")
    public void userResign(@AuthenticationPrincipal UserDetailsImpl userDetails) throws UnsupportedEncodingException {
        userService.userResign(userDetails);
    }
}
