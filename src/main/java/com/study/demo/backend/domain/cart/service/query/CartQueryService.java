package com.study.demo.backend.domain.cart.service.query;

import com.study.demo.backend.domain.cart.dto.response.CartResDTO;
import com.study.demo.backend.global.security.userdetails.AuthUser;

public interface CartQueryService {
    CartResDTO.CartInfo getCartInfo(AuthUser authUser);
}
