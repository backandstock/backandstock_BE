package com.project.minibacktesting_be.exception.user;


import lombok.Getter;

@Getter
public class UserRegisterValidationException extends RuntimeException{

    public UserRegisterValidationException(String message) {
        super(message);

    }
}
