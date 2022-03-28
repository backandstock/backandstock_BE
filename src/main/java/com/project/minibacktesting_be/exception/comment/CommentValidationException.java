package com.project.minibacktesting_be.exception.comment;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CommentValidationException extends RuntimeException{

    private String message;
    private String detail;
    public CommentValidationException(String message, String detail) {
        this.message = message;
        this.detail = detail;

    }
}