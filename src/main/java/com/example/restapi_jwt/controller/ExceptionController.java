package com.example.restapi_jwt.controller;

import com.example.restapi_jwt.entity.TokenDto;
import com.example.restapi_jwt.service.MemberService;
import com.example.restapi_jwt.service.RestComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private RestComponent restComponent;

    @GetMapping("/access")
    public ResponseEntity nonLogin(HttpServletRequest request) {

        String accessToken = request.getHeader("ACCESS-TOKEN");

        // 토큰이 존재한다면 (토큰이 만료 되었다면) -> ACCESS-TOKEN 신규 발급
        if (accessToken != null){
            String refreshToken = request.getHeader("REFRESH-TOKEN");
            TokenDto token = TokenDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();

            TokenDto newToken = memberService.issueAccessToken(token);
            return restComponent.getResponseEntity(newToken);
        }

        return  restComponent.getResponseEntity(Map.of("errCode", "goLoginView"));
    }

    @GetMapping("/denied")
    public ResponseEntity denied(){
        return  restComponent.getResponseEntity(Map.of("errCode", "Access-Denied"));
    }
}
