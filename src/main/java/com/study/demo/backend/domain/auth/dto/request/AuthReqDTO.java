package com.study.demo.backend.domain.auth.dto.request;

public class AuthReqDTO {

    public record Login(
            String email,
            String password
    ) {
    }
}
