package com.project.minibacktesting_be.exception.comment;

import lombok.Getter;


@Getter
public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(String message) {
        super(message);
    }
}
