package com.project.minibacktesting_be.exception.stock;

import lombok.Getter;

@Getter
public class StockNotFoundException extends RuntimeException{
    public StockNotFoundException(String message) {

        super(message);
    }

}

