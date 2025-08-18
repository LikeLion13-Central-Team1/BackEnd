package com.study.demo.backend.domain.cart.exception;

import com.study.demo.backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CartErrorCode implements BaseErrorCode {

    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "CART404_0","장바구니가 없습니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "CART404_1","메뉴를 찾을 수 없습니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "CART400_0","수량은 1 이상이어야 합니다."),
    CART_MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "CART404_2","장바구니에 해당 메뉴가 없습니다."),

    // 추가: 빈 장바구니 에러
    CART_EMPTY(HttpStatus.BAD_REQUEST, "CART400_1", "장바구니가 비어있습니다."),
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
