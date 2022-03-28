package com.project.minibacktesting_be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorResponse {
    private Integer statusCode;
    private HttpStatus httpStatus;
    private String responseMessage;

    public static ApiErrorResponse badRequest(String message) {
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, message);
    }

    public static ApiErrorResponse forbidden(String message) {
        return new ApiErrorResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, message);
    }

    public static ApiErrorResponse notFound(String message) {
        return new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, message);
    }

    public static ApiErrorResponse unauthorized(String message) {
        return new ApiErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, message);
    }
}