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
    INVALID_OPENING_CLOSING_TIME(HttpStatus.BAD_REQUEST, "STORE400_3", "유효하지 않은 시간 입력입니다."),
    STORE_STATUS_DENIED(HttpStatus.BAD_REQUEST,"STORE400_4", "이미 가게가 마감 상태이거나, 오픈 상태입니다."),

    STORE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "STORE403_0", "접근 권한(ROLE)이 없습니다."),
    NO_PERMISSION_TO_UPDATE_STORE(HttpStatus.FORBIDDEN, "STORE403_1", "가게를 수정할 권한이 없습니다."),

    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE404_0", "요청한 가게를 찾을 수 없습니다."),

    STORE_ALREADY_EXISTS(HttpStatus.CONFLICT, "STORE409_0", "이미 존재하는 가게입니다."),
    STORE_ALREADY_EXISTS_FOR_OWNER(HttpStatus.CONFLICT, "STORE409_1", "해당 사장님은 이미 가게를 보유하고 있습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
