package com.example.restapi_jwt.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class RestComponent {

    public ResponseEntity getResponseEntity(Object responseObj) {

        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        StringBuffer requestURL = request.getRequestURL();

        Map<String, Object> index = new HashMap<>();
        index.put("rel", "index");
        index.put("href",requestURL);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", responseObj);
        resultMap.put("links", Arrays.asList(index));

        return ResponseEntity.ok().body(resultMap);
    }
}
