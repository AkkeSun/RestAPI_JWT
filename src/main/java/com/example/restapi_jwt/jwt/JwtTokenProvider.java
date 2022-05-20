package com.example.restapi_jwt.jwt;

import com.example.restapi_jwt.entity.MemberDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.jwt.secretKey}")
    private String secretKey;

    private long tokenValidTime = 1000L * 60 * 1; // 1분

    private long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 7; // 7일

    private final UserDetailsService userDetailsService;

    /**
     * 토큰 생성하기
     * @param email
     * @return 토큰 인코딩 String
     */
    //========= ACCESS TOKEN ==========
    public String createToken(String email){

        Date now = new Date();

        // 토큰에 포함되는 정보 설정
        Claims claims = Jwts.claims().setSubject(email);
        // claims.put("roles", dto.getRole());

        return Jwts.builder()
                .setClaims(claims) // 토큰 정보 저장
                .setIssuedAt(now)  // 토큰 생성일
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // 토큰 만료일
                .signWith(SignatureAlgorithm.HS256, secretKey) // 인코딩 방식
                .compact();

    }

    //========= REFRESH TOKEN : Claims 를 입력하지 않는다 ==========
    public String createRefreshToken(){

        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)  // 토큰 생성일
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // 토큰 만료일
                .signWith(SignatureAlgorithm.HS256, secretKey) // 인코딩 방식
                .compact();
    }




    /**
     * 토큰값으로 인증객채 생성하기
     * @param token
     * @return 인증객채
     *
     * 1. Token 으로 Claims 가져오기
     * 2. Claims 로 이메일 가져오기
     * 3. 이메일로 인증객채(UserDetails) 가져오기
     * 4. UsernamePasswordAuthenticationToken 생성하기
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getMemberEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰값으로 Claims 가져오기
    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    // 토큰 값으로 이메일(Subject) 가져오기
    public String getMemberEmail(String token) {
        return getClaims(token).getSubject();
    }





    /**
     * 토큰 시간이 유효한지 확인
     * @param token
     * @return boolean
     */
    public boolean validateTokenExceptExpiration(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch(Exception e) {
            return false;
        }
    }

}
