package com.project.minibacktesting_be.exception.portfolio;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortfolioSaveOverException extends  RuntimeException{

    private long userId;
    private String message;

    public PortfolioSaveOverException(String message,long userId) {
        this.message = message;
        this.userId = userId;
    }
}
