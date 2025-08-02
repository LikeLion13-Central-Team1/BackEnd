package com.study.demo.backend.domain.auth.service.command;

public interface TokenCommandService {
    void deleteRefreshTokenByLoginId(String email);
    void addToBlacklist(String token, long durationMs);
}
