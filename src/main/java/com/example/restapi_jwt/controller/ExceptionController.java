package com.example.restapi_jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping("/login")
    public String nonLogin(){
        return "LoginView 로 이동합니다 (false 리턴)";
    }

    @GetMapping("/denied")
    public String denied(){
        return "권한이 없습니다 (false 리턴)";
    }
}
