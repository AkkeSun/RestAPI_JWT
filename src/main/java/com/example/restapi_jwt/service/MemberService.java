package com.example.restapi_jwt.service;

import com.example.restapi_jwt.entity.MemberDto;
import com.example.restapi_jwt.entity.Member_Jwt;
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

        return MemberDto.builder().email(save.getEmail()).password(save.getPassword()).build();
    }

    public void validateDuplicated(String email) {
        if (memberRepository.findByEmail(email).isPresent())
            throw new RuntimeException("중복된 Email 입니다");
    }


    @Transactional
    public String memberLogin(MemberDto dto){

        Member_Jwt member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(ArithmeticException::new);
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword()))
            throw new RuntimeException("아이디 혹은 비밀번호가 올바르지 않습니다");

        // 토큰 발급 후 리턴
        return jwtTokenProvider.createToken(dto);
    }

}
