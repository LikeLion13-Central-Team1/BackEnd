package com.study.demo.backend.domain.store.exception;

import com.study.demo.backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StoreErrorCode implements BaseErrorCode {


    INVALID_SORT_TYPE(HttpStatus.BAD_REQUEST, "STORE400_1", "유효하지 않은 정렬 타입입니다."),
    INVALID_LOCATION(HttpStatus.BAD_REQUEST, "STORE400_2", "위도/경도 정보가 유효하지 않습니다."),

    STORE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "STORE403_0", "접근 권한(ROLE)이 없습니다."),

    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE404_0", "요청한 가게를 찾을 수 없습니다."),

    STORE_ALREADY_EXISTS(HttpStatus.CONFLICT, "STORE409_0", "이미 존재하는 가게입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
