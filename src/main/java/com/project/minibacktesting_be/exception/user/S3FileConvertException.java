package com.project.minibacktesting_be.exception.user;

import lombok.Getter;

@Getter
public class S3FileConvertException extends RuntimeException{
    public  S3FileConvertException(String message) {
        super(message);
    }
}

