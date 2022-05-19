package com.example.restapi_jwt.entity;

import com.example.restapi_jwt.Role;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Getter
@Service
@Builder
public class MemberDto {
    private String email;
    private String password;
    private List<Role> roles;
}

