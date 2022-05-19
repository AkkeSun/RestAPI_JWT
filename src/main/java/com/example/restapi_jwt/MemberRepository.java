package com.example.restapi_jwt;

import com.example.restapi_jwt.entity.Member_Jwt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Member;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member_Jwt, Integer> {
    Optional<Member_Jwt> findByEmail(String email);
}
