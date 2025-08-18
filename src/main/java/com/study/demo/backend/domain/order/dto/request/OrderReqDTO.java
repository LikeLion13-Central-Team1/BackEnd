package com.study.demo.backend.domain.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class OrderReqDTO {

        @Schema(name = "주문 생성 요청 DTO")
        public record CreateOrder(
                @NotEmpty(message = "주문할 메뉴 정보가 비어있습니다.")
                @Schema(description = "주문 메뉴 목록")
                List<OrderItem> orderMenus,

                @NotNull(message = "픽업 시간을 입력해주세요.")
                @Future(message = "픽업 시간은 현재 시간 이후로 설정해야 합니다.")
                @Schema(description = "픽업 방문 시간", example = "2025-08-15T08:15:00")
                LocalDateTime visitTime
        ) {}

        @Schema(name = "주문 생성 요청 DTO")
        public record CreateOrderByCartId(
                @NotNull(message = "장바구니 ID는 필수입니다.")
                @Schema(description = "장바구니 ID", example = "1")
                Long cartId,

                @NotNull(message = "픽업 시간을 입력해주세요.")
                @Future(message = "픽업 시간은 현재 시간 이후로 설정해야 합니다.")
                @Schema(description = "픽업 방문 시간", example = "2025-08-15T08:15:00")
                LocalDateTime visitTime
        ) {}

        @Schema(name = "주문 메뉴 항목 DTO")
        public record OrderItem(
                @NotNull(message = "메뉴 ID는 필수입니다.")
                @Schema(description = "메뉴 ID", example = "1")
                Long menuId,

                @NotNull(message = "수량은 필수입니다.")
                @Positive(message = "수량은 1 이상이어야 합니다.")
                @Schema(description = "주문 수량", example = "3")
                int quantity
        ) {}
}
