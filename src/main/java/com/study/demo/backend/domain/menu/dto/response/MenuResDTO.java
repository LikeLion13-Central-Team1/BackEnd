package com.study.demo.backend.domain.menu.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class MenuResDTO {

    @Builder
    @Schema(name = "메뉴 등록 응답 DTO")
    public record CreateMenu(
            @Schema(description = "메뉴 ID", example = "1")
            Long menuId,

            @Schema(description = "메뉴 이름", example = "버섯 피자")
            String name,

            @Schema(description = "정가", example = "13000")
            BigDecimal price,

            @Schema(description = "할인 비율", example = "14")
            Integer discountPercent,

            @Schema(description = "할인 가격", example = "11180")
            BigDecimal discountPrice,

            @Schema(description = "메뉴 설명", example = "버섯과 치즈가 들어간 피자")
            String description,

            @Schema(description = "메뉴 이미지 URL", example = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Pizza-3007395.jpg/500px-Pizza-3007395.jpg")
            String menuImage,

            @Schema(description = "수량", example = "7")
            Integer quantity
    ) {}

    @Builder
    public record MenuDetail(
            Long menuId,
            String name,
            BigDecimal price,
            Integer discountPercent,
            String description,
            String menuImage,
            Integer quantity,
            LocalDateTime createdAt
    ) {}

    // 무한 스크롤, 커서 기반 페이지네이션 응답
    @Builder
    public record MenuDetailList(
            List<MenuDetail> menus,
            boolean hasData,
            Long nextCursor
    ) {}

    @Builder
    public record UpdateMenu(
            Long menuId,
            String name,
            BigDecimal price,
            Integer discountPercent,
            BigDecimal discountPrice,
            Integer quantity,
            String description,
            String menuImage
    ) {}
}
