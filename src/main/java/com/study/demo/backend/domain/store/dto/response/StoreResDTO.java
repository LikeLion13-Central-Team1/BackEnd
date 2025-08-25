package com.study.demo.backend.domain.store.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class StoreResDTO {

    @Builder
    public record CreateStoreRes(
            Long storeId,
            String name,
            LocalTime openingTime,
            LocalTime closingTime,
            BigDecimal latitude,
            BigDecimal longitude,
            LocalDateTime createdAt
    ){
    }

    @Builder
    public record StoreDetail(
            Long storeId,
            String name,
            String roadAddressName,
            String imageUrl,
            LocalTime openingTime,
            LocalTime closingTime,
            BigDecimal latitude,
            BigDecimal longitude,
            LocalDateTime createdAt,
            boolean favorited,
            boolean openStatus
    ) {}

    // 무한 스크롤, 커서 기반 페이지네이션을 위해 필요한 데이터
    public record StoreDetailList(
            List<StoreDetail> stores,
            boolean nextData,
            Long nextCursor
    ) {}

    @Builder
    public record UpdateStoreRes(
            Long storeId,
            String name,
            LocalTime openingTime,
            LocalTime closingTime,
            BigDecimal latitude,
            BigDecimal longitude,
            LocalDateTime updatedAt,
            String imageUrl,
            boolean openStatus
    ) {}

    @Builder
    public record CloseRes(
            Long storeId,
            LocalTime previousClosingTime,
            LocalTime newClosingTime,
            boolean openStatus
    ) {}

    @Builder
    public record OpenRes(
            Long storeId,
            LocalTime previousOpeningTime,
            LocalTime newOpeningTime,
            boolean openStatus
    ) {}
}
