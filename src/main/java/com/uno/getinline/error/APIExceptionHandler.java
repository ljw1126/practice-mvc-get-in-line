package com.uno.getinline.error;

import com.uno.getinline.constant.ErrorCode;
import com.uno.getinline.dto.ApiErrorResponse;
import com.uno.getinline.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class APIExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> general(GeneralException e){
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = errorCode.isClientSideError() ?
                HttpStatus.BAD_REQUEST :
                HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(status)
                .body(ApiErrorResponse.of(
                        false, errorCode, errorCode.getMessage()
                ));
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> exception(Exception e){
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(status)
                .body(ApiErrorResponse.of(
                        false, errorCode, errorCode.getMessage()
                ));
    }


}
