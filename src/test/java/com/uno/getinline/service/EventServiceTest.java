package com.uno.getinline.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.uno.getinline.constant.ErrorCode;
import com.uno.getinline.constant.EventStatus;
import com.uno.getinline.domain.Event;
import com.uno.getinline.dto.EventDTO;
import com.uno.getinline.exception.GeneralException;
import com.uno.getinline.repository.EventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

/**
 * mockito 사용하기 위해 아래  ExtendWith 추가, InjectMocks , Mock 애노테이션 설정
 */
@DisplayName("비즈니스 로직 - 이벤트")
@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks private EventService eventService;
    @Mock private EventRepository eventRepository;

    /*
    @BeforeEach
    void setUp(){
        eventService = new EventService();
    }
    */

    @DisplayName("이벤트를 검색하면, 결과를 출력하여 보여준다.")
    @Test
    void givenNothing_whenSearchingEvents_thenReturnsEntireEventList(){
        //given
        given(eventRepository.findAll(any(Predicate.class)))
                .willReturn(List.of(
                        createEvent(1L,"오전 운동",true),
                        createEvent(1L,"오후 운동",false)
                ));

        //when
        List<EventDTO> list = eventService.getEvents(null,null,null,null,null);

        //then
        assertThat(list).hasSize(2);
        then(eventRepository).should().findAll(any(Predicate.class));
    }

    /**
     * 1. org.mockito.exceptions.base.MockitoException:
     * Checked exception is invalid for this method!
     *
     * Exception e 사용시 위에 에러 출력됨
       2.  assertThat(throwable)
         .isInstanceOf(GeneralException.class)
         .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage()); 이해 안됨 ch03-03
     * >> EventService에 getEvents() 에 try catch문으로 에러발생시 GeneralException throw 하도록 변경
     */
    @DisplayName("이벤트 검색하는데 에러가 발생한 경우, 줄서기 프로젝트 기본 에러로 전환하여 예외던진다.")
    @Test
    void givenDataRelatedException_whenSearchingEvents_thenReturnsEntireEventList(){
        //given
        RuntimeException e = new RuntimeException("This is test");
        given(eventRepository.findAll(any(Predicate.class))).willThrow(e);

        //when (예외 받는 메소드 catchThrowable)
        Throwable throwable = catchThrowable(()-> eventService.getEvents(new BooleanBuilder()));

        //then
        assertThat(throwable)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(eventRepository).should().findAll(any(Predicate.class));
    }

    @DisplayName("이벤트 ID로 존재하는 이벤트를 조회하면, 해당 이벤트 정보를 출력하여 보여준다.")
    @Test
    void givenEventId_whenSearchingExistingEvent_thenReturnsEvent() {
        // Given
        long eventId = 1L;
        Event event = createEvent(1L, "오전 운동", true);
        given(eventRepository.findById(eventId))
                .willReturn(Optional.of(event));

        // When
        Optional<EventDTO> result = eventService.getEvent(eventId);

        // Then
        assertThat(result).hasValue(EventDTO.of(event));
        then(eventRepository).should().findById(eventId);
    }

    @DisplayName("이벤트 ID로 이벤트를 조회하면, 빈 정보를 출력하여 보여준다.")
    @Test
    void givenEventId_whenSearchingNonexistentEvent_thenReturnsEmptyOptional() {
        // Given
        long eventId = 2L;
        given(eventRepository.findById(eventId))
                .willReturn(Optional.empty());

        // When
        Optional<EventDTO> result = eventService.getEvent(eventId);

        // Then
        assertThat(result).isEmpty();
        then(eventRepository).should().findById(eventId);
    }

    @DisplayName("이벤트 ID로 이벤트를 조회하는데 데이터 관련 에러가 발생한 경우, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다.")
    @Test
    void givenDataRelatedException_whenSearchingEvent_thenThrowsGeneralException() {
        // Given
        RuntimeException e = new RuntimeException("This is test.");
        given(eventRepository.findById(any())).willThrow(e);

        // When
        Throwable thrown = catchThrowable(() -> eventService.getEvent(null));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(eventRepository).should().findById(any());
    }


    @DisplayName("이벤트 정보를 주면, 이벤트를 생성하고 결과를 true 로 보여준다.")
    @Test
    void givenEvent_whenCreating_thenCreatesEventAndReturnsTrue() {
        // Given
        Event event = createEvent(1L,"오후 운동", false);
        given(eventRepository.save(event)).willReturn(event);

        // When
        boolean result = eventService.createEvent(EventDTO.of(event));

        // Then
        assertThat(result).isTrue();
        then(eventRepository).should().save(event);
    }

    @DisplayName("이벤트 정보를 주지 않으면, 생성 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNothing_whenCreating_thenAbortCreatingAndReturnsFalse() {
        // Given

        // When
        boolean result = eventService.createEvent(null);

        // Then
        assertThat(result).isFalse();
        then(eventRepository).shouldHaveNoInteractions();
    }

    @DisplayName("이벤트 생성 중 데이터 예외가 발생하면, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다")
    @Test
    void givenDataRelatedException_whenCreating_thenThrowsGeneralException() {
        // Given
        Event event = createEvent(0L, null, false);
        RuntimeException e = new RuntimeException("This is test.");
        given(eventRepository.save(any())).willThrow(e);

        // When
        Throwable thrown = catchThrowable(() -> eventService.createEvent(EventDTO.of(event)));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(eventRepository).should().save(any());
    }

    @DisplayName("이벤트 ID와 정보를 주면, 이벤트 정보를 변경하고 결과를 true 로 보여준다.")
    @Test
    void givenEventIdAndItsInfo_whenModifying_thenModifiesEventAndReturnsTrue() {
        // Given
        long eventId = 1L;
        Event originalEvent = createEvent(1L, "오후 운동", false);
        Event changedEvent = createEvent(1L, "오전 운동", true);
        given(eventRepository.findById(eventId)).willReturn(Optional.of(originalEvent));
        given(eventRepository.save(changedEvent)).willReturn(changedEvent);

        // When
        boolean result = eventService.modifyEvent(eventId, EventDTO.of(changedEvent));

        // Then
        assertThat(result).isTrue();
        then(eventRepository).should().findById(eventId);
        then(eventRepository).should().save(changedEvent);
    }

    @DisplayName("이벤트 ID를 주지 않으면, 이벤트 정보 변경 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNoEventId_whenModifying_thenAbortModifyingAndReturnsFalse() {
        // Given
        Event event = createEvent(1L,"오후 운동", false);

        // When
        boolean result = eventService.modifyEvent(null, EventDTO.of(event));

        // Then
        assertThat(result).isFalse();
        then(eventRepository).shouldHaveNoInteractions();
    }

    @DisplayName("이벤트 ID만 주고 변경할 정보를 주지 않으면, 이벤트 정보 변경 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenEventIdOnly_whenModifying_thenAbortModifyingAndReturnsFalse() {
        // Given
        long eventId = 1L;

        // When
        boolean result = eventService.modifyEvent(eventId, null);

        // Then
        assertThat(result).isFalse();
        then(eventRepository).shouldHaveNoInteractions();
    }

    @DisplayName("이벤트 변경 중 데이터 오류가 발생하면, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다.")
    @Test
    void givenDataRelatedException_whenModifying_thenThrowsGeneralException() {
        // Given
        long eventId = 1L;
        Event originalEvent = createEvent(1L, "오후 운동", false);
        Event wrongEvent = createEvent(0L, null, false);
        RuntimeException e = new RuntimeException("This is test.");
        given(eventRepository.findById(eventId)).willReturn(Optional.of(originalEvent));
        given(eventRepository.save(any())).willThrow(e);

        // When
        Throwable thrown = catchThrowable(() -> eventService.modifyEvent(eventId, EventDTO.of(wrongEvent)));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(eventRepository).should().findById(eventId);
        then(eventRepository).should().save(any());
    }


    @DisplayName("이벤트 ID를 주면, 이벤트 정보를 삭제하고 결과를 true 로 보여준다.")
    @Test
    void givenEventId_whenDeleting_thenDeletesEventAndReturnsTrue() {
        // Given
        long eventId = 1L;
        willDoNothing().given(eventRepository).deleteById(eventId);

        // When
        boolean result = eventService.removeEvent(eventId);

        // Then
        assertThat(result).isTrue();
        then(eventRepository).should().deleteById(eventId);
    }

    @DisplayName("이벤트 ID를 주지 않으면, 삭제 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNothing_whenDeleting_thenAbortsDeletingAndReturnsFalse() {
        // Given

        // When
        boolean result = eventService.removeEvent(null);

        // Then
        assertThat(result).isFalse();
        then(eventRepository).shouldHaveNoInteractions();
    }

    @DisplayName("이벤트 삭제 중 데이터 오류가 발생하면, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다.")
    @Test
    void givenDataRelatedException_whenDeleting_thenThrowsGeneralException() {
        // Given
        long eventId = 0L;
        RuntimeException e = new RuntimeException("This is test.");
        willThrow(e).given(eventRepository).deleteById(eventId);

        // When
        Throwable thrown = catchThrowable(() -> eventService.removeEvent(eventId));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(eventRepository).should().deleteById(eventId);
    }


    private Event createEvent(long placeId, String eventName, boolean isMorning) {
        String hourStart = isMorning ? "09" : "13";
        String hourEnd = isMorning ? "12" : "16";

        return createEvent(
                placeId,
                eventName,
                EventStatus.OPENED,
                LocalDateTime.parse("2021-01-01T%s:00:00".formatted(hourStart)),
                LocalDateTime.parse("2021-01-01T%s:00:00".formatted(hourEnd))
        );
    }

    private Event createEvent(
            long placeId,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime
    ) {
        return Event.of(
                placeId,
                eventName,
                eventStatus,
                eventStartDateTime,
                eventEndDateTime,
                0,
                24,
                "마스크 꼭 착용하세요"
        );
    }



}