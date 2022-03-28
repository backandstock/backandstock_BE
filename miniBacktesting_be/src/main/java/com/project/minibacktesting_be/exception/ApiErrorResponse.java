package com.project.minibacktesting_be.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiErrorResponse {
    private String responseMessage;
    private String detail;
    private HttpStatus httpStatus;

    public ApiErrorResponse(String responseMessage, String detail, HttpStatus httpStatus) {
//        super();
        this.responseMessage = responseMessage;
        this.detail = detail;
        this.httpStatus = httpStatus;
    }
}