package com.study.demo.backend.domain.user.exception;

import com.study.demo.backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER400_3", "해당 Nickname이 이미 존재합니다."),
    CURRENT_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "USER400_5", "현재 비밀번호가 일치하지 않습니다."),
    NEW_PASSWORD_SAME_AS_CURRENT(HttpStatus.BAD_REQUEST, "USER400_6", "새 비밀번호가 현재 비밀번호와 동일합니다."),
    NEW_NICKNAME_SAME_AS_CURRENT(HttpStatus.BAD_REQUEST, "USER400_7", "새 닉네임이 현재 닉네임과 동일합니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404_0", "해당 사용자를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
