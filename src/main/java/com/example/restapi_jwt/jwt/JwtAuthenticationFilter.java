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
        String accessToken = ((HttpServletRequest) servletRequest).getHeader("ACCESS-TOKEN");

        // 토큰이 유효하다면 토큰으로부터 유저정보를 가져와 셋업한다
        if(accessToken != null && jwtTokenProvider.validateTokenExceptExpiration(accessToken)) {
            Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
