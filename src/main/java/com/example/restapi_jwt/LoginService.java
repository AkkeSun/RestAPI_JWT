package com.example.restapi_jwt;

import com.example.restapi_jwt.entity.MemberDto;
import com.example.restapi_jwt.entity.Member_Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class LoginService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public MemberDto memberRegister(MemberDto dto) {

        validateDuplicated(dto.getEmail());

        Member_Jwt member = Member_Jwt.builder()
                            .email(dto.getEmail())
                            .password(passwordEncoder.encode(dto.getPassword()))
                            .roles(Arrays.asList(Role.ROLE_ADMIN))
                            .build();
        Member_Jwt save = memberRepository.save(member);

        return MemberDto.builder().email(save.getEmail()).password(save.getPassword()).build();
    }

    public void validateDuplicated(String email) {
        if (memberRepository.findByEmail(email).isPresent())
            throw new RuntimeException("중복된 Email 입니다");
    }


    @Transactional
    public MemberDto memberLogin(MemberDto dto){
        Member_Jwt member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(ArithmeticException::new);
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword()))
            throw new RuntimeException("아이디 혹은 비밀번호가 올바르지 않습니다");
        return MemberDto.builder().email(member.getEmail()).password(member.getPassword()).roles(member.getRoles()).build();

    }


}
