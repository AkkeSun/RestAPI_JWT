package com.example.restapi_jwt.controller;

import com.example.restapi_jwt.entity.TokenDto;
import com.example.restapi_jwt.service.MemberService;
import com.example.restapi_jwt.service.RestComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private RestComponent restComponent;

    @PostMapping("/issueAccessToken")
    public ResponseEntity issueAccessToken(@RequestBody TokenDto tokenDto) {
        TokenDto token = memberService.issueAccessToken(tokenDto);
        return restComponent.getResponseEntity(token);
    }

}
