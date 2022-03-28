package com.project.minibacktesting_be.exception.comment;

import lombok.Getter;

@Getter
public class CommentValidationException extends RuntimeException{

    public CommentValidationException(String message) {
        super(message);

    }
}