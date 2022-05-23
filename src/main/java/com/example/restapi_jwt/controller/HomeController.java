package com.example.restapi_jwt.controller;

import com.example.restapi_jwt.service.RestComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @Autowired
    private RestComponent restComponent;

    @GetMapping("/hello")
    public ResponseEntity hello() {
        return restComponent.getResponseEntity(Map.of("val", "Hello!"));
    }

    @GetMapping("/admin")
    public ResponseEntity admin() {

        return restComponent.getResponseEntity(Map.of("val", "ADMIN!"));
    }
}




