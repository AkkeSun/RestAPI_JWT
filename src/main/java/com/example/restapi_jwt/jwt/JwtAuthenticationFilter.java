package com.example.restapi_jwt.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // HTTP Header 에 저장되어 있는 토큰 추출
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        System.out.println("TOKEN = " + token);

        // 토큰이 유효하다면 토큰으로부터 유저정보를 가져온다
        if(token != null && jwtTokenProvider.validateTokenExceptExpiration(token)){
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }
}
