package com.project.minibacktesting_be.exception;

import com.project.minibacktesting_be.exception.comment.CommentNotFoundException;
import com.project.minibacktesting_be.exception.comment.CommentValidationException;
import com.project.minibacktesting_be.exception.portfolio.PortfolioNotFoundException;
import com.project.minibacktesting_be.exception.portfolio.PortfolioSaveOverException;
import com.project.minibacktesting_be.exception.stock.StockNotFoundException;
import com.project.minibacktesting_be.exception.stock.StockSearchException;
import com.project.minibacktesting_be.exception.user.S3FileConvertException;
import com.project.minibacktesting_be.exception.user.UserMatchException;
import com.project.minibacktesting_be.exception.user.UserNotFoundException;
import com.project.minibacktesting_be.exception.user.UserRegisterValidationException;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(PortfolioNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> PortfolioNotFoundExceptionHandler(PortfolioNotFoundException exception) {
        log.info("PortfolioNotFoundExceptionHandler", exception);
        return new ResponseEntity<>(ApiErrorResponse.notFound(exception.getMessage()),HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(PortfolioSaveOverException.class)
    public ResponseEntity<ApiErrorResponse> PortfolioSaveOverExceptionHandler(PortfolioSaveOverException exception) {
        log.info("PortfolioSaveOverExceptionHandler", exception);
        return new ResponseEntity<>(ApiErrorResponse.badRequest(exception.getMessage()),HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(StockSearchException.class)
    public ResponseEntity<ApiErrorResponse> StockSearchExceptionHandler(StockSearchException exception) {
        log.info("StockSearchExceptionHandler", exception);
        return new ResponseEntity<>(ApiErrorResponse.badRequest(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> StockNotFoundExceptionHandler(StockNotFoundException exception) {
        log.info("StockNotFoundException", exception);
        return new ResponseEntity<>(ApiErrorResponse.notFound(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> CommentNotFoundExceptionHandler(CommentNotFoundException exception) {
        log.info("CommentNotFoundExceptionHandler", exception);
        return new ResponseEntity<> (ApiErrorResponse.notFound(exception.getMessage()),HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(CommentValidationException.class)
    public ResponseEntity<ApiErrorResponse> CommentValidationException(CommentValidationException exception) {
        log.info("CommentValidationException", exception);
        return new ResponseEntity<>(ApiErrorResponse.badRequest(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserMatchException.class)
    public ResponseEntity<ApiErrorResponse> UserMatchExceptionHandler(UserMatchException exception) {
        log.info("UserMatchExceptionHandler", exception);
        return new ResponseEntity<>(ApiErrorResponse.badRequest(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> UserNotFoundExceptionHandler(UserNotFoundException exception) {
        log.info("UserNotFoundExceptionHandler", exception);
        return new ResponseEntity<> (ApiErrorResponse.notFound(exception.getMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(S3FileConvertException.class)
    public ResponseEntity<ApiErrorResponse> S3FileConvertExceptionHandler(S3FileConvertException exception) {
        log.info("S3FileConvertExceptionHandler", exception);
        return new ResponseEntity<>(ApiErrorResponse.badRequest(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserRegisterValidationException.class)
    public ResponseEntity<ApiErrorResponse> UserRegisterValidationException(UserRegisterValidationException exception) {
        log.info("UserRegisterValidationException", exception);
        return new ResponseEntity<>(ApiErrorResponse.badRequest(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

}