package com.uno.getinline.constant;

import org.apache.tomcat.jni.Error;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ErrorCodeTest {
    /**
     * 파라미터 타입 테스트 방식으로 진행
     * ParameterizedTest, MethodSource 애노테이션 추가
     *  MethodSource("함수명지정")가능하나 선호 x
     * 밑에 Stream<Arguments> 함수를 매개변수로 입력받아 실행하는거 같은 느낌?
     *
     * ParameterizedTest(name = "[{index} {0} ===> {1}]") 표현식 이해 못함
     */
    @ParameterizedTest(name = "[{index} {0} ===> {1}]")
    @MethodSource
    @DisplayName("예외를 받으면, 예외 메시지가 포함된 메시지 출력")
    void givenExceptionWithMessage_whenGettingMessage_thenReturnsMessage(ErrorCode input, String expected){
        //given
        Exception e = new Exception("This is test message");
        //when
        String result = input.getMessage(e);
        //then
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> givenExceptionWithMessage_whenGettingMessage_thenReturnsMessage(){
        return Stream.of(
                arguments(ErrorCode.OK, "Ok - This is test message"),
                arguments(ErrorCode.BAD_REQUEST,"Bad request - This is test message"),
                arguments(ErrorCode.SPRING_BAD_REQUEST,"Spring-detected bad request - This is test message"),
                arguments(ErrorCode.VALIDATION_ERROR,"Validation error - This is test message"),
                arguments(ErrorCode.NOT_FOUND,"Requested resource is not found - This is test message"),
                arguments(ErrorCode.INTERNAL_ERROR,"Internal error - This is test message"),
                arguments(ErrorCode.SPRING_INTERNAL_ERROR,"Spring-detected internal error - This is test message"),
                arguments(ErrorCode.DATA_ACCESS_ERROR,"Data access error - This is test message")
        );
    }


    @ParameterizedTest(name = "[{index} \"{0}\" ===> \"{1}\"]")
    @MethodSource
    @DisplayName("에러 메시지를 받으면, 해당 에러 메시지를 출력")
    void givenMessage_whenGettingMessage_thenReturnsMessage(String input, String expected){
        //given

        //when
        String result = ErrorCode.INTERNAL_ERROR.getMessage(input);
        //then
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> givenMessage_whenGettingMessage_thenReturnsMessage(){
        return Stream.of(
                arguments(null, ErrorCode.INTERNAL_ERROR.getMessage() ),
                arguments("", ErrorCode.INTERNAL_ERROR.getMessage()),
                arguments(" ", ErrorCode.INTERNAL_ERROR.getMessage()),
                arguments("This is test message", "This is test message")
        );
    }

    @DisplayName("toString() 호출 포맷")
    @Test
    void givenErrorCode_whenToString_thenReturnsSimplefiedToString(){
        //given

        //when
        String result = ErrorCode.INTERNAL_ERROR.toString();

        //then
        assertThat(result).isEqualTo("INTERNAL_ERROR (20000)");
    }
}