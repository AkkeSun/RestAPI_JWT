package com.example.restapi_jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/exception")
public class ExceptionController {


    @GetMapping("/access")
    public String nonLogin(HttpServletRequest request) {

        String accessToken = request.getHeader("ACCESS-TOKEN");

        // 토큰이 존재한다면 (토큰이 만료 되었다면)
        if (accessToken != null)
            return "refresh token 요청 key";

        return "LoginView 로 이동 key";
    }

    @GetMapping("/denied")
    public String denied(){
        return "권한이 없습니다 (false 리턴)";
    }
}
