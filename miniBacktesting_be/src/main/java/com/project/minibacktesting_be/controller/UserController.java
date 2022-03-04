package com.project.minibacktesting_be.controller;

import com.project.minibacktesting_be.dto.LoginCheckDto;
import com.project.minibacktesting_be.dto.SignupDto;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import com.project.minibacktesting_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/user/signup")
    public void registerUser(@RequestBody SignupDto requestDto) {
        userService.registerUser(requestDto);
    }

    // 로그인 정보
    @PostMapping("/islogin")
    public LoginCheckDto userLoginCheck(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return new LoginCheckDto(userDetails.getUser());
    }

    // 회원정보 수정
    @PutMapping("/user/signup")
    public void userInfoEdit(){

    }
}
