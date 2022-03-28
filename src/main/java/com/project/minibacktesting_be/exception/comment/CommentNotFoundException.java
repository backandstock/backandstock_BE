package com.project.minibacktesting_be.exception.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentNotFoundException extends RuntimeException{
    private long id;

    public CommentNotFoundException(long id) {

        this.id = id;
    }

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
}
