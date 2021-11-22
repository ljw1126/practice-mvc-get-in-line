package com.uno.getinline.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BaseErrorController.class)
class BaseErrorControllerTest {

    private MockMvc mvc;

    // 생성자 주입방식
    public BaseErrorControllerTest(@Autowired MockMvc mvc){
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 에러 - 404 페이지 없음")
    @Test
    void givenWrongURI_whenRequestingRootPage_thenReturn404ErrorPage(@Autowired MockMvc mvc) throws Exception{
        // Given


        // When & Then
        mvc.perform(get("/waring-uri"))
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }

}