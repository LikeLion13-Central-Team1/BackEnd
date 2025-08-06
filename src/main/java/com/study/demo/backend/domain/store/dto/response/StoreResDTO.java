package com.study.demo.backend.domain.store.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class StoreResDTO {

    @Builder
    public record Create(
            Long storeId,
            String name,
            java.time.LocalTime openingTime,
            java.time.LocalTime closingTime,
            java.math.BigDecimal latitude,
            java.math.BigDecimal longitude,
            LocalDateTime createdAt
    ){
    }

    @Builder
    public record StoreDetail(
            Long storeId,
            String name,
            java.time.LocalTime openingTime,
            java.time.LocalTime closingTime,
            java.math.BigDecimal latitude,
            java.math.BigDecimal longitude,
            LocalDateTime createdAt
    ) {}

    // 무한 스크롤, 커서 기반 페이지네이션을 위해 필요한 데이터
    public record StoreDetailList(
            List<StoreDetail> stores,
            boolean nextData,
            Long nextCursor
    ) {}

    @Builder
    public record Update(
            Long storeId,
            String name,
            java.time.LocalTime openingTime,
            java.time.LocalTime closingTime,
            java.math.BigDecimal latitude,
            java.math.BigDecimal longitude,
            java.time.LocalDateTime updatedAt
    ) {}

}
