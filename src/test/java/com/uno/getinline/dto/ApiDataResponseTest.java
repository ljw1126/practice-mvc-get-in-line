package com.uno.getinline.dto;

import com.uno.getinline.constant.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ApiDataResponseTest {

    @DisplayName("문자열 데이터 주어지면, 표준 성공 응답을 생성한다.")
    @Test
    void givenStringData_whenCreatingResponse_thenReturnsSuccessfulResponse(){
        //given
        String data = "test data";

        //when
        ApiDataResponse<String> response = ApiDataResponse.of(data);

        //then
        assertThat(response)
                //.isNotNull()
                .hasFieldOrPropertyWithValue("success", true)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.OK.getCode())
                .hasFieldOrPropertyWithValue("message", ErrorCode.OK.getMessage())
                .hasFieldOrPropertyWithValue("data", data);

    }


    @DisplayName("데이터가 없을때, 비어있는 표준응답 생성한다.")
    @Test
    void givenNothing_whenCreatingResponse_thenReturnsEmptySuccessfulResponse(){
        //given
        String data = "test data";

        //when
        ApiDataResponse<String> response = ApiDataResponse.empty();

        //then
        assertThat(response)
                //.isNotNull()
                .hasFieldOrPropertyWithValue("success", true)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.OK.getCode())
                .hasFieldOrPropertyWithValue("message", ErrorCode.OK.getMessage())
                .hasFieldOrPropertyWithValue("data", null);

    }
}