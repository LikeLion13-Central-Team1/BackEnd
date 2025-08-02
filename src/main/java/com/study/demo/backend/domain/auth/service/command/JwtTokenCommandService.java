package com.study.demo.backend.domain.auth.service.command;

import com.study.demo.backend.global.security.utils.JwtUtil;
import com.study.demo.backend.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenCommandService implements TokenCommandService{

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private static final String BLACKLIST_PREFIX = "blacklist:";

    @Override
    public void deleteRefreshTokenByLoginId(String email) {
        log.info("이메일에 대한 토큰을 삭제합니다: {}", email);
        redisUtil.delete(email + ":refresh");
    }

    @Override
    public void addToBlacklist(String token, long durationMs) {
        String key = BLACKLIST_PREFIX + token;
        redisUtil.save(key, true, durationMs, TimeUnit.MILLISECONDS);
    }
}
