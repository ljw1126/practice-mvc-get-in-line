package com.uno.getinline.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@AutoConfigureMockMvc
//@SpringBootTest
@WebMvcTest(BaseController.class)
class BaseControllerTest {

    private MockMvc mvc;

    // 생성자 주입방식
    public BaseControllerTest(@Autowired MockMvc mvc){
        this.mvc = mvc;
    }

    // basePageShouldShowIndexPage
    @DisplayName("[view][GET] 기본페이지 요청")
    @Test
    void givenNothing_whenRequestingRootPage_thenReturnIdnexPage(@Autowired MockMvc mvc) throws Exception{
        // Given


        // When & Then
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("Hello world")))
                .andExpect(view().name("index"))
                .andDo(print())
        ;
    }
}