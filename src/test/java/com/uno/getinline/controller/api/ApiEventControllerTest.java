package com.uno.getinline.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uno.getinline.constant.ErrorCode;
import com.uno.getinline.constant.EventStatus;
import com.uno.getinline.dto.EventDTO;
import com.uno.getinline.dto.EventResponse;
import com.uno.getinline.service.EventService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled("API 컨트롤러가 필요없는 상황이어서 비활성화")
@DisplayName("API 컨트롤러 - 이벤트")
@WebMvcTest(ApiEventController.class)
class ApiEventControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    @MockBean
    private EventService eventService;

    public ApiEventControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper
    ) {
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @DisplayName("[API][GET] 이벤트 리스트 조회 + 검색 파라미터")
    @Test
    void givenParameters_whenRequestingEvents_thenReturnsListOfEventsInStandardResponse() throws Exception {
        // Given
        given(eventService.getEvents(any(),any(),any(),any(),any()))
                .willReturn(List.of(createEventDTO()));

        // When & Then
        mvc.perform(get("/api/events")
                        .queryParam("placeId","1")
                        .queryParam("eventName","운동")
                        .queryParam("eventStatus",EventStatus.OPENED.name())
                        .queryParam("eventStartDatetime","2021-01-01T00:00:00")
                        .queryParam("eventEndDatetime","2021-01-02T00:00:00")

                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].placeId").value(1L))
                .andExpect(jsonPath("$.data[0].eventName").value("오후 운동"))
                .andExpect(jsonPath("$.data[0].eventStatus").value(EventStatus.OPENED.name()))
                .andExpect(jsonPath("$.data[0].eventStartDatetime").value(LocalDateTime
                        .of(2021, 1, 1, 13, 0, 0)
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.data[0].eventEndDatetime").value(LocalDateTime
                        .of(2021, 1, 1, 16, 0, 0)
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.data[0].currentNumberOfPeople").value(0))
                .andExpect(jsonPath("$.data[0].capacity").value(24))
                .andExpect(jsonPath("$.data[0].memo").value("마스크 꼭 착용하세요"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));

        then(eventService).should().getEvents(any(),any(),any(),any(),any());
    }

    @DisplayName("[API][GET] 이벤트 리스트 조회 - 잘못된 검색 파라미터//ch03-02validation")
    @Test
    void givenWrongParameters_whenRequestingEvents_thenReturnsFailedStandardResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(
                        get("/api/events")
                                .queryParam("placeId", "0")
                                .queryParam("eventName", "오")
                                .queryParam("eventStatus",EventStatus.OPENED.name())
                                .queryParam("eventStartDatetime","2021-01-01T00:00:00")
                                .queryParam("eventEndDatetime","2021-01-02T00:00:00")
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(containsString(ErrorCode.VALIDATION_ERROR.getMessage())))
                .andDo(print());   // containsString() 일부만 맞아도

        then(eventService).shouldHaveNoInteractions();
    }



    @DisplayName("[API][POST] 이벤트 생성")
    @Test
    void givenEvent_whenCreatingAnEvent_thenReturnsSuccessfulStandardResponse() throws Exception {
        // Given
        EventResponse eventResponse = EventResponse.of(
                1L,
                "오후 운동",
                EventStatus.OPENED,
                LocalDateTime.of(2021, 1, 1, 13, 0, 0),
                LocalDateTime.of(2021, 1, 1, 16, 0, 0),
                0,
                24,
                "마스크 꼭 착용하세요"
        );
        given(eventService.createEvent(any())).willReturn(true);

        // When & Then
//        mvc.perform(
//                        post("/api/events")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(mapper.writeValueAsString(eventResponse))
//                )
//                .andExpect(status().isCreated())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.data").value(Boolean.TRUE.toString()))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
//                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
//        then(eventService).should().createEvent(any());
    }


    @DisplayName("[API][POST] 이벤트 생성- 잘못된 데이터 입력")
    @Test
    void givenWrongEvent_whenCreatingAnEvent_thenReturnsSuccessfulStandardResponse() throws Exception {
        // Given  ( 일부러 틀린 값만 테스트 )
        EventResponse eventResponse = EventResponse.of(
                -1L,
                " ",
                null,
                null,
                null,
                -1,
                0,
                "마스크 꼭 착용하세요"
        );

        /**
         * VALIDATION_ERROR확인시 출력되는 10001 - Spring-detected bad request 은 MethodArgumentNotValidException 로 spring에서 관리하는 에러
         * 생성해둔 예외처리 핸들러 APIExceptionHandler.java 에서
         * Exception을 다루는 handleExceptionInternal()로 에러가 들어가 10001(SPRING_BAD_REQUEST, 임의지정) 을 보내줌
                 */
        // When & Then
        mvc.perform(
                        post("/api/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(eventResponse))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.SPRING_BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.message").value(containsString(ErrorCode.SPRING_BAD_REQUEST.getMessage())));
        then(eventService).shouldHaveNoInteractions();
    }





    private EventDTO createEventDTO(){
        return  EventDTO.of(
                1L,
                1L,
                "오후 운동",
                EventStatus.OPENED,
                LocalDateTime.of(2021, 1, 1, 13, 0, 0),
                LocalDateTime.of(2021, 1, 1, 16, 0, 0),
                0,
                24,
                "마스크 꼭 착용하세요",
                LocalDateTime.now(),
                LocalDateTime.now());
    }

}