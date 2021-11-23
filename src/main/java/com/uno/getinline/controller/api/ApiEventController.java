package com.uno.getinline.controller.api;

import com.uno.getinline.constant.EventStatus;
import com.uno.getinline.dto.ApiDataResponse;
import com.uno.getinline.dto.EventRequest;
import com.uno.getinline.dto.EventResponse;
import com.uno.getinline.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ApiEventController {

    private final EventService eventService;

    @GetMapping("/events")
    public ApiDataResponse<List<EventResponse>> getEvents(
            @Positive Long placeId,
            @Size(min=2) String eventName,
            EventStatus eventStatus,
            @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventStartDatetime,
            @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventEndDatetime
    ){
        // EventResponse <- EventDTO 데이터를 변환하는 책임을  EventResponse에 추가
        List<EventResponse> responses = eventService
                .getEvents(
                            placeId,
                            eventName,
                            eventStatus,
                            eventStartDatetime,
                            eventEndDatetime
                ).stream().map(EventResponse::from).toList();

        return ApiDataResponse.of(responses);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events")
    public ApiDataResponse<String> createEvent(
            @Valid @RequestBody EventRequest eventRequest,
            @PathVariable Long placeId
    ){
        boolean result = eventService.createEvent(eventRequest.toDto());
        return ApiDataResponse.of(Boolean.toString(result));
    }

    @GetMapping("/events/{eventId}")
    public ApiDataResponse<EventResponse> getEvent(
            @PathVariable Long eventId
    ){
        if (eventId.equals(2L)) {
            return ApiDataResponse.empty();
        }

        return ApiDataResponse.of(EventResponse.of(
                1L,
                "오후 운동",
                EventStatus.OPENED,
                LocalDateTime.of(2021, 1, 1, 13, 0, 0),
                LocalDateTime.of(2021, 1, 1, 16, 0, 0),
                0,
                24,
                "마스크 꼭 착용하세요"
        ));
    }

    @PutMapping("/events/{eventId}")
    public ApiDataResponse<Void> modifyEvent(
            @PathVariable Long eventId,
            @RequestBody EventRequest eventRequest
    ){
        return ApiDataResponse.empty();
    }

    @DeleteMapping("/events/{eventId}")
    public ApiDataResponse<Void> removeEvent(@PathVariable Long eventId){
        return ApiDataResponse.empty();
    }


}
