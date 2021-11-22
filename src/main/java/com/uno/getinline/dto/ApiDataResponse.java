

package com.uno.getinline.dto;

import com.uno.getinline.constant.ErrorCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ApiDataResponse<T> extends ApiErrorResponse{

    private final T data;
    // 정상상황 가정하고 만든거
    private ApiDataResponse(T data){
        super(true, ErrorCode.OK.getCode(), ErrorCode.OK.getMessage());
        this.data = data;
    }

    public static <T> ApiDataResponse<T> of(T data){
        return new ApiDataResponse(data);
    }

    public static <T> ApiDataResponse<T> empty(){
        return new ApiDataResponse(null);
    }

}
