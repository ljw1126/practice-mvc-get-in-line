package com.uno.getinline.controller;

import com.uno.getinline.service.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    private final MockMvc mvc;

    @MockBean
    private EventService eventService;

    public EventControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 이벤트 리스트 페이지")
    @Test
    void givenNothing_whenRequestingEventsPage_thenReturnsEventsPage() throws Exception {
        // Given

        // When & Then
       /* mvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("event/index"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("events"));*/

    }

    @DisplayName("[view][GET] 이벤트 세부 정보 페이지")
    @Test
    void givenEventId_whenRequestingEventDetailPage_thenReturnsEventDetailPage() throws Exception {
        // Given
        long eventId = 1L;

        // When & Then
        mvc.perform(get("/events/" + eventId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("event/detail"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("event"));
    }


}
