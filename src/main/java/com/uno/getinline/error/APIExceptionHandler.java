package com.uno.getinline.error;

import com.uno.getinline.constant.ErrorCode;
import com.uno.getinline.dto.ApiErrorResponse;
import com.uno.getinline.exception.GeneralException;
import org.hibernate.validator.internal.util.annotation.ConstraintAnnotationDescriptor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice(annotations = {RestController.class, RepositoryRestController.class})
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * ch03-02 validation 테스트 중 추가, ConstraintViolationException 수동 처리 필요
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request){
        return getInternalResponseEntity(e, request, HttpHeaders.EMPTY, ErrorCode.VALIDATION_ERROR, HttpStatus.BAD_REQUEST);
    }

    /**
     * ResponseEntityExceptionHandler 상속 받으면 스프링에서 관리하는 exception 처리 가능(하지만 깔끔하지 않음)
     * protected 되어 있는 ResponseEntity<Object> handleExceptionInternal
     */

    @ExceptionHandler
    public ResponseEntity<Object> general(GeneralException e, WebRequest request){
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = errorCode.isClientSideError() ?
                HttpStatus.BAD_REQUEST :
                HttpStatus.INTERNAL_SERVER_ERROR;

        return getInternalResponseEntity(e, request, HttpHeaders.EMPTY,errorCode, status);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e,WebRequest request){
         return getInternalResponseEntity(e, request, HttpHeaders.EMPTY,ErrorCode.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ControllerAdvie(4)
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorCode errorCode = status.is4xxClientError()?
                ErrorCode.SPRING_BAD_REQUEST : ErrorCode.SPRING_INTERNAL_ERROR;
        // 상속 내용 중 5xx 에러 처리하는 부분 사용하고 싶으므로 super사용
        return getInternalResponseEntity(e, request, headers,errorCode, status);
    }


    private ResponseEntity<Object> getInternalResponseEntity(Exception e, WebRequest request, HttpHeaders headers,  ErrorCode errorCode, HttpStatus status) {
        return super.handleExceptionInternal(
                e,
                ApiErrorResponse.of(false, errorCode.getCode(), errorCode.getMessage(e) ),
                HttpHeaders.EMPTY,
                status,
                request
        );
    }
}
