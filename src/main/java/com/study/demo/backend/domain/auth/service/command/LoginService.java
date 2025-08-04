package com.study.demo.backend.domain.auth.service.command;

import com.study.demo.backend.global.security.dto.JwtDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {
    JwtDTO loginAsCustomer(String email, HttpServletResponse response);
    JwtDTO loginAsOwner(String email, HttpServletResponse response);
}
