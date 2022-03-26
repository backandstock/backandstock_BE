package com.project.minibacktesting_be.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// exception 발생시 에러와 status를 보내준다.
@RestControllerAdvice
public class RestApiExceptionHandler {
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleApiRequestException(IllegalArgumentException ex) {
        RestApiException restApiException = new RestApiException();
        restApiException.setHttpStatus(HttpStatus.BAD_REQUEST);
        restApiException.setErrorMessage(ex.getMessage());
        return new ResponseEntity(
                restApiException,
                HttpStatus.BAD_REQUEST
        );

    }
}