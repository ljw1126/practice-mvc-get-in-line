package com.uno.getinline.controller.api;

import com.uno.getinline.constant.PlaceType;
import com.uno.getinline.dto.ApiDataResponse;
import com.uno.getinline.dto.PlaceDTO;
import com.uno.getinline.dto.PlaceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RequestMapping("/api")
//@RestController
public class ApiPlaceController {

   /* @GetMapping("/places")
    public ApiDataResponse<List<PlaceDTO>> getPlaces(){
        return ApiDataResponse.of(List.of(PlaceDTO.of(
                          PlaceType.COMMON,
                "발리 배드민턴장",
                   "서울시 강남구 강남대로 1234",
                "010-1234-5678",
                    29,
                    "신장개업"
        )));
    }*/

    @GetMapping("/places")
    public ApiDataResponse<List<PlaceResponse>> getPlaces(){
        return ApiDataResponse.of(List.of(PlaceResponse.of(
                PlaceType.COMMON,
                "발리 배드민턴장",
                "서울시 강남구 강남대로 1234",
                "010-1234-5678",
                29,
                "신장개업"
        )));
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/places")
    public ApiDataResponse<Void> createPlace(){
        return ApiDataResponse.empty();
    }

    @GetMapping("/places/{placeId}")
    public ApiDataResponse<PlaceDTO> getPlace(@PathVariable Integer placeId) {

        if(placeId.equals(2)){ // 빈값 테스트
            return ApiDataResponse.of(null);
        }

        return ApiDataResponse.of(PlaceDTO.of(
                PlaceType.COMMON,
                "발리 배드민턴장",
                "서울시 강남구 강남대로 1234",
                "010-1234-5678",
                29,
                "신장개업"
        ));
    }

    @PutMapping("/places/{placeId}")
    public Boolean modifyPlace(@PathVariable Integer placeId){
        return true;
    }

    @DeleteMapping("/places/{placeId}")
    public Boolean removePlace(@PathVariable Integer placeId){
        return true;
    }


}
