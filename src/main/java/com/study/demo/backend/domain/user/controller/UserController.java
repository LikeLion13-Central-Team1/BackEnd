package com.study.demo.backend.domain.user.controller;

import com.study.demo.backend.domain.user.dto.response.UserResDTO;
import com.study.demo.backend.domain.user.service.query.UserQueryService;
import com.study.demo.backend.global.apiPayload.CustomResponse;
import com.study.demo.backend.global.security.annotation.CurrentUser;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserQueryService userQueryService;

    @GetMapping("")
    @Operation()
    public CustomResponse<UserResDTO.UserInfo> getUserInfo(@CurrentUser AuthUser authUser) {
        UserResDTO.UserInfo response = userQueryService.getUserInfo(authUser.getEmail());
        return CustomResponse.onSuccess(response);
    }
}
