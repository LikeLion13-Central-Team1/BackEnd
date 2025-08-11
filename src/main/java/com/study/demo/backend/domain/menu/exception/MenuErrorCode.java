package com.study.demo.backend.domain.menu.exception;

import com.study.demo.backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MenuErrorCode implements BaseErrorCode {


    INVALID_SORT_TYPE(HttpStatus.BAD_REQUEST, "MENU400_1", "유효하지 않은 정렬 타입입니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "MENU400_2", "유효하지 않은 가격입니다."),
    INVALID_DISCOUNT_PERCENT(HttpStatus.BAD_REQUEST, "MENU400_3", "할인율은 0~100 사이 값 입니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "MENU400_4","잘못된 수량 값 입니다." ),


    MENU_ACCESS_DENIED(HttpStatus.FORBIDDEN, "MENU403_0", "접근 권한(ROLE)이 없습니다."),

    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "MENU404_0", "요청한 메뉴가 존재하지 않습니다."),
    MENU_STORE_MISMATCH(HttpStatus.NOT_FOUND, "MENU404_1", "해당 가게에 존재하지 않는 메뉴입니다."),

    MENU_ALREADY_EXISTS(HttpStatus.CONFLICT, "MENU409_0", "이미 존재하는 메뉴입니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
