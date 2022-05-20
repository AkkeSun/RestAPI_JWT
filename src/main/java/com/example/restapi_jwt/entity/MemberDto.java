package com.example.restapi_jwt.entity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@Getter
@NoArgsConstructor
public class MemberDto {

    private String email;
    private String password;
    private String role;

    private String refreshToken;

    @Builder
    public MemberDto(String email, String password, String role, String refreshToken){
        this.email = email;
        this.password = password;
        this.role = role;
        this.refreshToken = refreshToken;
    }

}

