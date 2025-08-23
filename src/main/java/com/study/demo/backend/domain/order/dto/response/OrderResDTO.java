package com.study.demo.backend.domain.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResDTO {
    @Builder
    @Schema(name = "주문 생성 응답 DTO")
    public record CreateOrder(
            @Schema(description = "생성된 주문 ID", example = "1")
            Long orderId,

            @Schema(description = "주문 번호", example = "d8b36871-382c-453c-8729-53723a595a45")
            String orderNum,

            @Schema(description = "주문 생성 시각")
            LocalDateTime orderTime,

            @Schema(description = "픽업 방문 시각")
            LocalDateTime visitTime,

            @Schema(description = "총 주문 금액", example = "25500")
            BigDecimal totalPrice,

            @Schema(description = "가게 이름", example = "XXX 피자")
            String storeName
    ) {}

    @Builder
    @Schema(name = "주문 내역 조회 응답 DTO")
    public record OrderDetail(
            @Schema(description = "주문 ID", example = "1")
            Long orderId,

            @Schema(description = "주문 번호", example = "htm3k")
            String orderNum,

            @Schema(description = "가게 이름", example = "현우의 왕 피자")
            String storeName,

            @Schema(description = "총 주문 금액", example = "26000")
            BigDecimal totalPrice,

            @Schema(description = "주문 생성 시각")
            LocalDateTime orderTime,

            @Schema(description = "방문 시각")
            LocalDateTime visitTime,

            @Schema(description = "주문한 메뉴 목록 ")
            List<String> menuSummaries,

            @Schema(description = "총 정가", example = "30000")
            BigDecimal totalOriginalPrice,

            @Schema(description = "총 할인 금액", example = "4000")
            BigDecimal totalDiscountAmount,

            @Schema(description = "평균 할인율(%)", example = "13.33")
            BigDecimal averageDiscountPercent
    ) {}

    @Builder
    @Schema(name = "주문 내역 목록 조회 응답 DTO")
    public record OrderList(
            @Schema(description = "주문 내역 리스트")
            List<OrderDetail> orderList,

            @Schema(description = "다음 페이지 조회를 위한 커서 ID. 다음 페이지가 없으면 -1")
            Long nextCursor
    ) {}
}
