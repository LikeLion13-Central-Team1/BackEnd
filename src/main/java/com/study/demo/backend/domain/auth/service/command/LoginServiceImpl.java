package com.study.demo.backend.domain.auth.service.command;

import com.study.demo.backend.domain.auth.exception.AuthErrorCode;
import com.study.demo.backend.domain.auth.exception.AuthException;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.domain.user.repository.UserRepository;
import com.study.demo.backend.global.security.dto.JwtDTO;
import com.study.demo.backend.global.security.userdetails.CustomUserDetails;
import com.study.demo.backend.global.security.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public JwtDTO loginAsCustomer(String email, HttpServletResponse response) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(AuthErrorCode.MEMBER_NOT_FOUND));
        return generateToken(user, response);
    }

    @Override
    public JwtDTO loginAsOwner(String email, HttpServletResponse response) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(AuthErrorCode.MEMBER_NOT_FOUND));
        return generateToken(user, response);
    }

    private JwtDTO generateToken(User user, HttpServletResponse response) {
        // CustomUserDetails 생성
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // JWT 토큰 생성
        String accessToken = jwtUtil.createJwtAccessToken(userDetails);
        String refreshToken = jwtUtil.createJwtRefreshToken(userDetails);

        setCookies(response, accessToken, refreshToken);

        return JwtDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void setCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        // 액세스 토큰 쿠키
        Cookie accessCookie = new Cookie("access_token", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(Math.toIntExact(jwtUtil.getAccessExpMs() / 1000));
        accessCookie.setSecure(true);

        // 리프레시 토큰 쿠키
        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(Math.toIntExact(jwtUtil.getRefreshExpMs() / 1000));
        refreshCookie.setSecure(true);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }
}
