package com.study.demo.backend.domain.favorite.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FavoriteResDTO {

    public record Status(
            boolean favoritedStatus
    ) {}

    @Builder
    @Schema(name = "찜한 메뉴")
    public record FavoriteMenu(
            @Schema(description = "찜 ID", example = "2")
            Long favoriteId,

            @Schema(description = "메뉴 ID", example = "10")
            Long menuId,

            @Schema(description = "메뉴 이름", example = "버섯 피자")
            String menuName,

            @Schema(description = "가게 ID", example = "3")
            Long storeId,

            @Schema(description = "가게 이름", example = "킹왕짱피자")
            String storeName,

            @Schema(description = "정가", example = "13000")
            BigDecimal price,

            @Schema(description = "할인 비율(%)", example = "14")
            Integer discountPercent,

            @Schema(description = "할인가", example = "11180")
            BigDecimal discountPrice,

            @Schema(description = "메뉴 이미지 URL", example = "https://.../pizza.jpg")
            String menuImage,

            @Schema(description = "찜한 시각", example = "2025-08-18T00:12:00")
            LocalDateTime createdAt
    ) {}

    @Builder
    @Schema(name = "메뉴 찜 목록")
    public record FavoriteMenuList(
            @Schema(description = "찜한 메뉴 리스트")
            List<FavoriteMenu> FavoriteMenus,

            @Schema(description = "다음 요청에 넘길 cursor(favoriteId). 더 없으면 null", example = "30")
            Long nextCursor,

            @Schema(description = "추가 데이터 존재 여부", example = "true")
            boolean hasData
    ) {}

    @Builder
    @Schema(name = "찜한 가게 아이템")
    public record FavoriteStore(
            @Schema(description = "찜 ID", example = "13")
            Long favoriteId,

            @Schema(description = "가게 ID", example = "5")
            Long storeId,

            @Schema(description = "가게 이름", example = "킹왕짱피자")
            String storeName,

            @Schema(description = "가게 대표 이미지", example = "https://.../store.jpg")
            String storeImage,

            @Schema(description = "찜한 시각", example = "2025-08-18T00:12:00")
            LocalDateTime createdAt
    ) {}

    @Builder
    @Schema(name = "가게 찜 목록")
    public record FavoriteStoreList(
            @Schema(description = "찜한 가게 리스트")
            List<FavoriteStore> FavoriteStores,

            @Schema(description = "다음 요청에 넘길 cursor(favoriteId). 더 없으면 null", example = "54")
            Long nextCursor,

            @Schema(description = "추가 데이터 존재 여부", example = "false")
            boolean hasData
    ) {}


}
