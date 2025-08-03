package com.study.demo.backend.domain.auth.exception;

import com.study.demo.backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    OAUTH_EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER400_1", "이메일 정보를 찾을 수 없습니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "MEMBER400_3", "해당 Nickname이 이미 존재합니다."),
    CURRENT_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "MEMBER400_5", "현재 비밀번호가 일치하지 않습니다."),
    NEW_PASSWORD_SAME_AS_CURRENT(HttpStatus.BAD_REQUEST, "MEMBER400_6", "새 비밀번호가 현재 비밀번호와 동일합니다."),
    NEW_NICKNAME_SAME_AS_CURRENT(HttpStatus.BAD_REQUEST, "MEMBER400_7", "새 닉네임이 현재 닉네임과 동일합니다."),

    OAUTH_LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "MEMBER401_0", "로그인에 실패하였습니다."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404_0", "해당 사용자를 찾을 수 없습니다."),
    OAUTH_USER_INFO_FAIL(HttpStatus.NOT_FOUND, "MEMBER404_2", "사용자 정보 조회 실패"),
    PROFILE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404_3", "프로필 이미지를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
