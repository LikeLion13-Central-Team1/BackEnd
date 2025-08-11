package com.study.demo.backend.domain.user.exception;

import com.study.demo.backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserLocationErrorCode implements BaseErrorCode {

    USER_LOCATION_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_LOCATION_404_0", "해당 사용자위치를 찾을 수 없습니다."),
    LOCATION_ACCESS_DENIED(HttpStatus.FORBIDDEN,"USER_LOCATION_403_0", "해당 위치에 접근 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
