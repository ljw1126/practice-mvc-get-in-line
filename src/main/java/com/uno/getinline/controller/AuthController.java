package com.uno.getinline.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class AuthController {

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/sign-up")
    public String signUp(){
        return "auth/sign-up";
    }

}
