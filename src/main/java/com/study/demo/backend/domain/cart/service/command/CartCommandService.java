package com.study.demo.backend.domain.cart.service.command;

import com.study.demo.backend.domain.cart.dto.requset.CartReqDTO;
import com.study.demo.backend.domain.cart.dto.response.CartResDTO;
import com.study.demo.backend.global.security.userdetails.AuthUser;

public interface CartCommandService {
    CartResDTO.AddMenu addMenuToCart(CartReqDTO.AddMenu reqDTO, AuthUser authUser);
}
