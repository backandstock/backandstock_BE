package com.project.minibacktesting_be.exception.stock;

import lombok.Getter;

@Getter
public class StockSearchException extends RuntimeException{
    public StockSearchException(String message) {

        super(message);
    }

}