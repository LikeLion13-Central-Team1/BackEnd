package com.study.demo.backend.domain.review.exception;

import com.study.demo.backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements BaseErrorCode {

    REVIEW_ALREADY_EXISTS_FOR_ORDER(HttpStatus.BAD_REQUEST, "REVIEW400_1","해당 주문에 이미 리뷰가 존재합니다."),
    USER_NOT_MATCHED_WITH_ORDER(HttpStatus.BAD_REQUEST, "REVIEW400_1","주문을 생성한 사용자만 리뷰를 작성할 수 있습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
