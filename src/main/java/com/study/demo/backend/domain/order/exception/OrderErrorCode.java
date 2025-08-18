package com.study.demo.backend.domain.order.exception;

import com.study.demo.backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements BaseErrorCode {
    MENU_NOT_FOUND_IN_ORDER(HttpStatus.BAD_REQUEST, "ORDER400_0", "주문 요청에 존재하지 않는 메뉴가 포함되어 있습니다."),
    VISIT_TIME_ERROR(HttpStatus.BAD_REQUEST, "ORDER400_1", "픽업 시간은 현재 시간 이후로 설정해야 합니다."),

    ORDER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "ORDER403_0", "주문에 대한 접근 권한이 없습니다."),

    // 추가: 장바구니 관련 에러
    CART_ACCESS_DENIED(HttpStatus.FORBIDDEN, "ORDER403_1", "다른 사용자의 장바구니에 접근할 수 없습니다."),

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER404_0", "주문 정보 없음"),

    INSUFFICIENT_STOCK(HttpStatus.CONFLICT, "ORDER409_0", "메뉴의 재고가 부족하여 주문할 수 없습니다."),

    // 추가: 새로운 에러 코드들
    DIFFERENT_STORE_MENUS(HttpStatus.CONFLICT, "ORDER409_1", "서로 다른 가게의 메뉴는 함께 주문할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
