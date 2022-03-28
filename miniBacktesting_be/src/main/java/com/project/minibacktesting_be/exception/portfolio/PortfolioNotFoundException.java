package com.project.minibacktesting_be.exception.portfolio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortfolioNotFoundException extends RuntimeException{
    private long id;

    public PortfolioNotFoundException(long id) {
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
