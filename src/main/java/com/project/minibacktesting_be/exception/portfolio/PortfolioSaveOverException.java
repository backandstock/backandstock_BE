package com.project.minibacktesting_be.exception.portfolio;


import lombok.Getter;


@Getter
public class PortfolioSaveOverException extends  RuntimeException{


    public PortfolioSaveOverException(String message) {
        super(message);
    }
}
