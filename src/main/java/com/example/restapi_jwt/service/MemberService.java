package com.example.restapi_jwt.service;

import com.example.restapi_jwt.entity.MemberDto;
import com.example.restapi_jwt.entity.Member_Jwt;
import com.example.restapi_jwt.entity.TokenDto;
import com.example.restapi_jwt.jwt.JwtTokenProvider;
import com.example.restapi_jwt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public MemberDto memberRegister(MemberDto dto) {

        validateDuplicated(dto.getEmail());

        Member_Jwt member = Member_Jwt.builder()
                            .email(dto.getEmail())
                            .password(passwordEncoder.encode(dto.getPassword()))
                            .roles(Collections.singletonList(dto.getRole()))
                            .build();
        Member_Jwt save = memberRepository.save(member);

        return MemberDto.builder().email(save.getEmail()).password(save.getPassword()).role(save.getRoles().get(0)).build();
    }

    public void validateDuplicated(String email) {
        if (memberRepository.findByEmail(email).isPresent())
            throw new RuntimeException("중복된 Email 입니다");
    }


    @Transactional
    public MemberDto memberLogin(MemberDto dto){

        Member_Jwt member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("아이디 혹은 비밀번호가 올바르지 않습니다"));

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword()))
            throw new RuntimeException("아이디 혹은 비밀번호가 올바르지 않습니다");

        // refresh token DB 저장
        member.setRefreshToken(jwtTokenProvider.createRefreshToken());
        memberRepository.save(member);

        System.err.println("LOGIN SUCCESS");
        System.err.println("REFERSH-TOKEN : " + member.getRefreshToken());

        //access token (password 에 저장) 발급 후 리턴
        return MemberDto.builder()
                .email(member.getEmail())
                .password(jwtTokenProvider.createToken(dto.getEmail()))
                .role(member.getRoles().get(0))
                .refreshToken(member.getRefreshToken())
                .build();
    }

    @Transactional
    public TokenDto issueAccessToken (TokenDto tokenDto){
        System.err.println("[issueAccessToken]");

        // STEP 1 : refresh token 이 만료되었는지 확인
        if(!jwtTokenProvider.validateTokenExceptExpiration(tokenDto.getRefreshToken()))
            throw new RuntimeException("Refresh Token 이 만료되었습니다");

        // STEP 2 : 유저가 보낸 refresh 토큰과 db에 저장된 refresh 토큰이 같은지 확인
        Member_Jwt member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh Token 이 유효하지 않습니다"));
        System.err.println("[Refresh Token Check Success]");

        // STEP 3 : Access Token 신규 발급
        String newAccessToken = jwtTokenProvider.createToken(member.getEmail());
        String newRefreshToken = jwtTokenProvider.createRefreshToken();

        member.setRefreshToken(newRefreshToken);
        memberRepository.save(member);

        System.err.println("[New Access Token Created]");
        return TokenDto.builder().refreshToken(newRefreshToken).accessToken(newAccessToken).build();
    }

}
