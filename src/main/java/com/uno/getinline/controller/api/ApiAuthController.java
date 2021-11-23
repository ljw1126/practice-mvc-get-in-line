package com.uno.getinline.controller.api;

import com.uno.getinline.dto.AdminRequest;
import com.uno.getinline.dto.ApiDataResponse;
import com.uno.getinline.dto.LoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Deprecated
//@RequestMapping("/api")
//@RestController
public class ApiAuthController {

    @PostMapping("/sign-up")
    public ApiDataResponse<String> signUp(@RequestBody AdminRequest adminRequest){
        return ApiDataResponse.empty();
    }

    @PostMapping("/login")
    public ApiDataResponse<String> login(@RequestBody LoginRequest loginRequest){
        return ApiDataResponse.empty();
    }

}
