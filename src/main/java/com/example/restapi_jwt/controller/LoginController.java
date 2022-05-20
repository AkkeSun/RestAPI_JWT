package com.example.restapi_jwt.controller;

import com.example.restapi_jwt.entity.MemberDto;
import com.example.restapi_jwt.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sign")
public class LoginController {

    private final MemberService loginService;

    @PostMapping("/register")
    public MemberDto register(@RequestBody MemberDto dto) {
        return loginService.memberRegister(dto);
    }

    @PostMapping("/login")
    public MemberDto login(@RequestBody MemberDto dto) {
        return loginService.memberLogin(dto);
    }

}
