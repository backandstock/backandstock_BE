package com.project.minibacktesting_be.exception;

import com.project.minibacktesting_be.exception.comment.CommentNotFoundException;
import com.project.minibacktesting_be.exception.portfolio.PortfolioNotFoundException;
import com.project.minibacktesting_be.exception.portfolio.PortfolioSaveOverException;
import com.project.minibacktesting_be.exception.stock.StockSearchException;
import com.project.minibacktesting_be.exception.user.S3FileConvertException;
import com.project.minibacktesting_be.exception.user.UserMatchException;
import com.project.minibacktesting_be.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
// exception 발생시 에러와 status를 보내준다.
//@RestControllerAdvice
//public class RestApiExceptionHandler {
//    @ExceptionHandler(value = {IllegalArgumentException.class})
//    public ResponseEntity<Object> handleApiRequestException(IllegalArgumentException ex) {
//        RestApiException restApiException = new RestApiException();
//        restApiException.setHttpStatus(HttpStatus.BAD_REQUEST);
//        restApiException.setErrorMessage(ex.getMessage());
//        return new ResponseEntity(
//                restApiException,
//                HttpStatus.BAD_REQUEST
//        );
//    }
//}


@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(PortfolioNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> PortfolioNotFoundExceptionHandler(PortfolioNotFoundException exception) {
        ApiErrorResponse response =
                new ApiErrorResponse("Portfolio repository cannot find portId",
                        "No Portfolio is found with ID : " + exception.getId(),
                        HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(PortfolioSaveOverException.class)
    public ResponseEntity<ApiErrorResponse> PortfolioSaveOverExceptionHandler(PortfolioSaveOverException exception) {
        ApiErrorResponse response =
                new ApiErrorResponse(exception.getMessage(),
                        "User : " + exception.getUserId() +
                                " exceed portfolio saving limit.",
                        HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(StockSearchException.class)
    public ResponseEntity<ApiErrorResponse> StockSearchExceptionHandler(StockSearchException exception) {
        ApiErrorResponse response =
                new ApiErrorResponse(exception.getMessage(),
                        exception.getDetail(),
                        HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> CommentNotFoundExceptionHandler(CommentNotFoundException exception) {
        ApiErrorResponse response =
                new ApiErrorResponse("Comment Repository Cannot Found ID",
                        "No comment is found with ID : " + exception.getId(),
                        HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UserMatchException.class)
    public ResponseEntity<ApiErrorResponse> UserMatchExceptionHandler(UserMatchException exception) {
        ApiErrorResponse response =
                new ApiErrorResponse(exception.getMessage(),
                        "LoginUserId : " + exception.getLoginUserId() +
                                " not equal to targetUserId : " + exception.getTargetUserId(),
                        HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> UserNotFoundExceptionHandler(UserNotFoundException exception) {
        ApiErrorResponse response =
                new ApiErrorResponse(exception.getMessage(),
                        "UserDetails' value is null",
                        HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(S3FileConvertException.class)
    public ResponseEntity<ApiErrorResponse> S3FileConvertExceptionHandler(S3FileConvertException exception) {
        ApiErrorResponse response =
                new ApiErrorResponse(exception.getMessage(),
                        "Image file converting error occur",
                        HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



}