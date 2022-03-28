package com.project.minibacktesting_be.exception.stock;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StockSearchException extends RuntimeException{
    String message;
    String detail;
    public StockSearchException(String message,
                                String detail) {

        this.message = message;
        this.detail = detail;
    }

}