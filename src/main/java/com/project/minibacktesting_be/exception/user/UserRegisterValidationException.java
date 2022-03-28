package com.project.minibacktesting_be.exception.user;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserRegisterValidationException extends RuntimeException{

    private String message;
    private String detail;
    public UserRegisterValidationException(String message, String detail) {
        this.message = message;
        this.detail = detail;

    }
}
