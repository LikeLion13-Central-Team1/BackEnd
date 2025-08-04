package com.study.demo.backend.domain.order.exception;

import com.study.demo.backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements BaseErrorCode {

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER404_0", "주문 정보 없음"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
