package com.study.demo.backend.domain.auth.service.query;

public interface TokenQueryService {
    String getRefreshTokenByEmail(String email);
    boolean isTokenBlacklisted(String token);
}
