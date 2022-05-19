package com.example.restapi_jwt;

import com.example.restapi_jwt.entity.MemberDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.jwt.secretKey}")
    private String secretKey;

    private long tokenValidTime = 1000L * 60 * 30; // 30분

    private final UserDetailsService userDetailsService;

    /**
     * 토큰 생성하기
     * @param dto
     * @return 토큰 인코딩 String
     */
    public String createToken(MemberDto dto){

        return Jwts.builder()
                // header
                .setHeaderParam("typ", "JWT") // 토큰타입
                .setSubject(dto.getEmail())  // 토큰명

                // payload
                .setClaims(getClaims(dto)) // 토큰에 포함되는 정보

                // signature
                .signWith(SignatureAlgorithm.HS256, secretKey) // 인코딩 방식
                .compact();
    }



    private Claims getClaims (MemberDto dto){
        Date now = new Date();
        return Jwts.claims().setSubject(dto.getEmail())
                .setIssuedAt(now) // 토큰 생성일
                .setExpiration(new Date(now.getTime() + tokenValidTime)); // 토큰 만료일
    }

    // 토큰 시간이 유효한지 확인
    public boolean validateTokenExceptExpiration(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch(Exception e) {
            return false;
        }
    }


    // 토큰값으로 Claims 가져오기
    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }



    public String getEmailFromToken(String token) {
        return (String)getClaims(token).get("email");
    }


    public List<Role> getMemberRoleFromToken(String token) {
        return (List<Role>)getClaims(token).get("role");
    }
}
