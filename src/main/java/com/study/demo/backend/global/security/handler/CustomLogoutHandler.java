package com.study.demo.backend.global.security.handler;

import com.study.demo.backend.domain.auth.service.command.JwtTokenCommandService;
import com.study.demo.backend.domain.auth.service.query.JwtTokenQueryService;
import com.study.demo.backend.global.security.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.security.SignatureException;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtTokenCommandService jwtTokenCommandService;
    private final JwtTokenQueryService jwtTokenQueryService;
    private final JwtUtil jwtUtil;
    private final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    private final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 1. 요청에서 쿠키 추출
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                } else if (REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        // 2. 토큰 처리
        if (accessToken != null) {
            try {
                // 액세스 토큰에서 email 추출
                String email = jwtUtil.getEmail(accessToken);

                // Redis에서 저장된 리프레시 토큰 가져오기
                if (refreshToken == null) {
                    refreshToken = jwtTokenQueryService.getRefreshTokenByEmail(email);
                }

                if (refreshToken != null) {
                    // 리프레시 토큰을 블랙리스트에 추가
                    long expiryDuration = jwtUtil.getRefreshTokenRemainingTime(refreshToken);
                    jwtTokenCommandService.addToBlacklist(refreshToken, expiryDuration);
                }

                // 이메일과 연결된 리프레시 토큰 삭제
                jwtTokenCommandService.deleteRefreshTokenByLoginId(email);

            } catch (SignatureException | ExpiredJwtException | MalformedJwtException e) {
                // 토큰이 유효하지 않더라도 로그아웃은 계속 진행
                log.warn("Invalid token during logout: {}", e.getMessage());
                // 토큰이 유효하지 않으므로 서버 측 정리는 건너뛰고 쿠키만 삭제
            }
        }

        // 3. 클라이언트에서 쿠키 삭제
        deleteTokenCookies(response);
    }

    // 4. 응답에서 토큰 쿠키 삭제
    private void deleteTokenCookies(HttpServletResponse response) {
        // 액세스 토큰 쿠키 삭제
        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, null);
        accessTokenCookie.setMaxAge(0); // 즉시 만료
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        response.addCookie(accessTokenCookie);

        // 리프레시 토큰 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, null);
        refreshTokenCookie.setMaxAge(0); // 즉시 만료
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        response.addCookie(refreshTokenCookie);
    }

}