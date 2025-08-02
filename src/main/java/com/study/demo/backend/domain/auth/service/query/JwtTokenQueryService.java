package com.study.demo.backend.domain.auth.service.query;

import com.study.demo.backend.global.security.utils.JwtUtil;
import com.study.demo.backend.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenQueryService implements TokenQueryService{

    private final RedisUtil redisUtil;
    private static final String BLACKLIST_PREFIX = "blacklist:";

    @Override
    public String getRefreshTokenByEmail(String email) {
        return (String) redisUtil.get(email + ":refresh");
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisUtil.hasKey(key));
    }
}
