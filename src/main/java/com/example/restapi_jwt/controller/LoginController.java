package com.example.restapi_jwt.controller;

import com.example.restapi_jwt.entity.MemberDto;
import com.example.restapi_jwt.service.MemberService;
import com.example.restapi_jwt.service.RestComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sign")
public class LoginController {

    private final MemberService loginService;

    @Autowired
    public RestComponent restComponent;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody MemberDto dto) {
        MemberDto memberDto = loginService.memberRegister(dto);
        return restComponent.getResponseEntity(memberDto);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberDto dto) {
        MemberDto memberDto = loginService.memberLogin(dto);
        return restComponent.getResponseEntity(memberDto);
    }
}
