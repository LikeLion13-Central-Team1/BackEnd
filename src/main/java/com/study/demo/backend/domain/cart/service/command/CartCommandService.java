package com.study.demo.backend.domain.cart.service.command;

import com.study.demo.backend.domain.cart.dto.requset.CartReqDTO;
import com.study.demo.backend.domain.cart.dto.response.CartResDTO;
import com.study.demo.backend.global.security.userdetails.AuthUser;

public interface CartCommandService {
    CartResDTO.CartInfo addMenuToCart(CartReqDTO.AddMenu reqDTO, AuthUser authUser);
    CartResDTO.CartInfo updateMenuQuantity(CartReqDTO.UpdateMenuQuantity reqDTO, AuthUser authUser);
    CartResDTO.CartInfo deleteMenuFromCart(CartReqDTO.DeleteMenu reqDTO, AuthUser authUser);
}
