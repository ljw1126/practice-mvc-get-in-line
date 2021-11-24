package com.uno.getinline.error;

import com.uno.getinline.constant.ErrorCode;
import com.uno.getinline.dto.ApiErrorResponse;
import com.uno.getinline.exception.GeneralException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import javax.validation.ConstraintViolationException;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class APIExceptionHandlerTest {

    private APIExceptionHandler sut;
    private WebRequest webRequest;

    @BeforeEach
    void setUp(){
        sut = new APIExceptionHandler();
        webRequest = new DispatcherServletWebRequest(new MockHttpServletRequest());
    }

    @DisplayName("검증 오류 - 응답 데이터 정의")
    @Test
    void givenValidationException_whenCallingValidation_thenResturnResponseEntity(){
        //given
        ConstraintViolationException e = new ConstraintViolationException(Set.of());

        //when
        ResponseEntity<Object> response = sut.validation(e, webRequest);

        //then ( 에러 호출시 아래와 같은 명세로 나오는 것을 테스트 )
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", ApiErrorResponse.of(false, ErrorCode.VALIDATION_ERROR,e))
                .hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.BAD_REQUEST);
    }


    @DisplayName("프로젝트 일반 오류 - 응답 데이터 정의")
    @Test
    void givenGeneralException_whenCallingValidation_thenResturnResponseEntity(){
        //given
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        GeneralException e = new GeneralException(errorCode);

        //when
        ResponseEntity<Object> response = sut.general(e, webRequest);

        //then ( 에러 호출시 아래와 같은 명세로 나오는 것을 테스트 )
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", ApiErrorResponse.of(false,errorCode,e))
                .hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @DisplayName("기타(전체) 오류 - 응답 데이터 정의")
    @Test
    void givenOtherException_whenCallingValidation_thenResturnResponseEntity(){
        //given
        Exception e = new Exception();

        //when
        ResponseEntity<Object> response = sut.exception(e, webRequest);

        //then ( 에러 호출시 아래와 같은 명세로 나오는 것을 테스트 )
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", ApiErrorResponse.of(false,ErrorCode.INTERNAL_ERROR,e))
                .hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}