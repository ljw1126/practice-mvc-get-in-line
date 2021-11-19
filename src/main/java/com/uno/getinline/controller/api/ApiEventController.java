package com.uno.getinline.controller.api;

import com.uno.getinline.exception.GeneralException;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class ApiEventController {

    @GetMapping("/events")
    public void getEvents(){
        throw new GeneralException("테스트 에러 메시지");
        //return List.of("event1","event2");
    }

    @PostMapping("/events")
    public void createEvent(){
        throw new RuntimeException("런타임 에러 메시지");
        //return true;
    }

    @GetMapping("/events/{eventId}")
    public String getEvent(@PathVariable Integer eventId){
        return "event : " + eventId;
    }

    @PutMapping("/events/{eventId}")
    public Boolean modifyEvent(@PathVariable Integer eventId){
        return true;
    }

    @DeleteMapping("/events/{eventId}")
    public boolean removeEvent(@PathVariable Integer eventId){
        return true;
    }


}
