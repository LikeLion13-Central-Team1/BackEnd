package com.study.demo.backend.domain.cart.controller;

import com.study.demo.backend.domain.cart.dto.requset.CartReqDTO;
import com.study.demo.backend.domain.cart.dto.response.CartResDTO;
import com.study.demo.backend.domain.cart.service.command.CartCommandService;
import com.study.demo.backend.domain.cart.service.query.CartQueryService;
import com.study.demo.backend.global.apiPayload.CustomResponse;
import com.study.demo.backend.global.security.annotation.CurrentUser;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
@Tag(name = "장바구니 API", description = "장바구니 관련 API")
public class CartController {

    private final CartCommandService cartCommandService;
    private final CartQueryService cartQueryService;

    @PostMapping("")
    @Operation(summary = "장바구니 생성/메뉴추가 API by 김지명", description = "장바구니를 생성하여 메뉴를 추가합니다.")
    public CustomResponse<CartResDTO.AddMenu> addMenuToCart(@RequestBody CartReqDTO.AddMenu reqDTO,
                                                            @CurrentUser AuthUser authUser) {
        CartResDTO.AddMenu resDTO = cartCommandService.addMenuToCart(reqDTO, authUser);
        return CustomResponse.onSuccess(resDTO);
    }
}
