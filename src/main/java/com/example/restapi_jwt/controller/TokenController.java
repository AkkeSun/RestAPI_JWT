package com.example.restapi_jwt.controller;

import com.example.restapi_jwt.entity.TokenDto;
import com.example.restapi_jwt.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/issueAccessToken")
    public TokenDto issueAccessToken(@RequestBody TokenDto tokenDto) {
        return memberService.issueAccessToken(tokenDto);
    }

}
