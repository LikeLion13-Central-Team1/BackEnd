package com.study.demo.backend.domain.auth.controller;

import com.study.demo.backend.domain.auth.service.command.LoginService;
import com.study.demo.backend.global.apiPayload.CustomResponse;
import com.study.demo.backend.global.security.dto.JwtDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "인증 API", description = "인증 관련 API")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;

    @PostMapping("/login/customer")
    @Operation(summary = "일반 유저로 로그인 API by 김지명", description = "일반 유저 JWT 토큰을 쿠키에 넣어줍니다.")
    public CustomResponse<JwtDTO> loginAsCustomer(HttpServletResponse response) {
        log.info("테스트용 일반 유저 로그인 요청");
        String email = "test-customer@gmail.com";
        JwtDTO jwtDTO =loginService.loginAsCustomer(email, response);
        return CustomResponse.onSuccess(jwtDTO);
    }

    @PostMapping("/login/owner")
    @Operation(summary = "사장님 유저로 로그인 API by 김지명", description = "사장님 유저 JWT 토큰을 쿠키에 넣어줍니다.")
    public CustomResponse<JwtDTO> loginAsOwner(HttpServletResponse response) {
        log.info("테스트용 사장님 유저 로그인 요청");
        String email = "test-owner@gmail.com";
        JwtDTO jwtDTO =loginService.loginAsOwner(email, response);
        return CustomResponse.onSuccess(jwtDTO);
    }

    // Swagger용 컨트롤러
    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API by 김지명", description = "현재 로그인된 사용자의 토큰을 무효화하고 로그아웃 처리합니다.")
    public CustomResponse<String> logout() {return CustomResponse.onSuccess("로그아웃이 완료되었습니다.");}
}
