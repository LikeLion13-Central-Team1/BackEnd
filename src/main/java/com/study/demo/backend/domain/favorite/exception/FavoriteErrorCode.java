package com.study.demo.backend.domain.favorite.exception;

import com.study.demo.backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FavoriteErrorCode implements BaseErrorCode {
    FAVORITE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "FAVORITE403_0", "접근 권한(ROLE)이 없습니다."),

    ALREADY_FAVORITED(HttpStatus.CONFLICT, "FAVORITE409_0", "이미 찜한 항목입니다."),
    ALREADY_UNFAVORITED(HttpStatus.CONFLICT, "FAVORITE409_1", "이미 찜이 해제되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}