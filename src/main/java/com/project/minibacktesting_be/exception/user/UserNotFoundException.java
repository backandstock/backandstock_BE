package com.project.minibacktesting_be.exception.user;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {

        super(message);
    }
}
