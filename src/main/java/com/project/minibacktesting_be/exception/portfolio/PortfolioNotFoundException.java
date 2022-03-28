package com.project.minibacktesting_be.exception.portfolio;

import lombok.Getter;

@Getter
public class PortfolioNotFoundException extends RuntimeException{
    private String message;
    public PortfolioNotFoundException(String message) {

        this.message = message;
    }

}
