package com.project.minibacktesting_be.security;

import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@Slf4j
public class FormLoginAuthProvider implements AuthenticationProvider {

    @Resource(name="userDetailsServiceImpl")
    private UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    public FormLoginAuthProvider(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        // FormLoginFilter 에서 생성된 토큰으로부터 아이디와 비밀번호를 조회함
        String username = token.getName();
        String password = (String) token.getCredentials();

        log.info("auth : " + username +" 변수타입 : " +username.getClass().getName());
        log.info("auth : " + password +" 변수타입 : " +password.getClass().getName());

        // UserDetailsService 를 통해 DB에서 username 으로 사용자 조회

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        log.info("userDetails : " + userDetails);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {

            log.info("userDetails.getPassword() : " +userDetails.getPassword());
            log.info("Auth에서 비밀번호 일치 확인 : " + passwordEncoder.matches(password, userDetails.getPassword()));

            throw new BadCredentialsException(userDetails.getUsername() + "Invalid password");
        }
        log.info("UsernamePasswordAuthenticationToken 성공" );
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}