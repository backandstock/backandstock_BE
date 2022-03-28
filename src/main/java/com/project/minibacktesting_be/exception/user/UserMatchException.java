package com.project.minibacktesting_be.exception.user;

import lombok.Getter;


@Getter
public class UserMatchException extends RuntimeException {


    public UserMatchException(String message) {
        super(message);
    }

}
