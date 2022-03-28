package com.project.minibacktesting_be.exception.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMatchException extends RuntimeException {
    private String message;
    private long loginUserId;
    private long targetUserId;


    public UserMatchException(String message,
                              long loginUserId,
                              long targetUserId) {
        this.message = message;
        this.loginUserId = loginUserId;
        this.targetUserId = targetUserId;
    }

}
