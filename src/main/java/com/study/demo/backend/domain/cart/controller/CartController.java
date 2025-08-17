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
import org.springframework.web.bind.annotation.*;

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
    public CustomResponse<CartResDTO.CartInfo> addMenuToCart(@RequestBody CartReqDTO.AddMenu reqDTO,
                                                             @CurrentUser AuthUser authUser) {
        CartResDTO.CartInfo resDTO = cartCommandService.addMenuToCart(reqDTO, authUser);
        return CustomResponse.onSuccess(resDTO);
    }

    @GetMapping("")
    @Operation(summary = "장바구니 조회 API by 김지명", description = "장바구니를 조회합니다.")
    public CustomResponse<CartResDTO.CartInfo> getCartInfo(@CurrentUser AuthUser authUser) {
        CartResDTO.CartInfo resDTO = cartQueryService.getCartInfo(authUser);
        return CustomResponse.onSuccess(resDTO);
    }

    @PatchMapping("/menu")
    @Operation(summary = "장바구니 메뉴 수량 수정 API by 김지명", description = "장바구니에 있는 특정 메뉴의 수량을 수정합니다.")
    public CustomResponse<CartResDTO.CartInfo> updateMenuQuantity(@RequestBody CartReqDTO.UpdateMenuQuantity reqDTO,
                                                                  @CurrentUser AuthUser authUser) {
        CartResDTO.CartInfo resDTO = cartCommandService.updateMenuQuantity(reqDTO, authUser);
        return CustomResponse.onSuccess(resDTO);
    }

    @DeleteMapping("/menu")
    @Operation(summary = "장바구니 메뉴 삭제 API by 김지명", description = "장바구니에서 특정 메뉴를 삭제합니다.")
    public CustomResponse<CartResDTO.CartInfo> deleteMenuFromCart(@RequestBody CartReqDTO.DeleteMenu reqDTO,
                                                                  @CurrentUser AuthUser authUser) {
        CartResDTO.CartInfo resDTO = cartCommandService.deleteMenuFromCart(reqDTO, authUser);
        return CustomResponse.onSuccess(resDTO);
    }
}
