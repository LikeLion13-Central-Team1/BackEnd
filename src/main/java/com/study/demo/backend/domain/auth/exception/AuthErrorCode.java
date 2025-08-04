package com.study.demo.backend.domain.auth.exception;

import com.study.demo.backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "AUTH400_3", "해당 Nickname이 이미 존재합니다."),
    CURRENT_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "AUTH400_5", "현재 비밀번호가 일치하지 않습니다."),

    OAUTH_LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "AUTH401_0", "로그인에 실패하였습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH404_0", "해당 사용자를 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH404_1", "이메일 정보를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
