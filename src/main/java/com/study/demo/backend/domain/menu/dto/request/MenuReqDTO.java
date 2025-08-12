package com.study.demo.backend.domain.menu.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

public class MenuReqDTO {

        @Schema(name = "메뉴 등록 요청 DTO")
        public record CreateMenu(
                @NotBlank(message = "메뉴 이름은 필수입니다.")
                @Schema(description = "메뉴 이름", example = "버섯 피자")
                String name,

                @NotNull(message = "메뉴 정가는 필수입니다.")
                @DecimalMin(value = "0.0", inclusive = false, message = "정가는 0보다 커야 합니다.")
                @Schema(description = "메뉴 정가", example = "13000")
                BigDecimal price,

                @NotNull @Min(0) @Max(100)
                @Schema(description = "할인 비율 (%)", example = "14")
                Integer discountPercent,

                @NotNull(message = "할인 가격은 필수입니다.")
                @DecimalMin(value = "0.0", message = "할인 가격은 0 이상이어야 합니다.")
                @Schema(description = "할인 가격", example = "11180")
                BigDecimal discountPrice,

                @NotNull(message = "수량을 필수입니다.")
                @Schema(description = "수량", example = "5")
                Integer quantity,

                @Schema(description = "메뉴 설명", example = "버섯과 치즈가 들어간 피자")
                String description
        ) {
        }

        @Builder
        @Schema(name = "메뉴 전체(부분) 수정 요청 DTO")
        public record UpdateMenu(
                @Schema(description = "메뉴 이름", example = "콤비네이션 스페셜")
                String name,

                @DecimalMin(value = "0.0", inclusive = false, message = "정가는 0보다 커야 합니다.")
                @Schema(description = "정가", example = "16000")
                BigDecimal price,

                @Min(0) @Max(100)
                @Schema(description = "할인율(%)", example = "15")
                Integer discountPercent,

                @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
                @Schema(description = "재고 수량", example = "25")
                Integer quantity,

                @Schema(description = "설명", example = "토핑 가득 베스트 업그레이드")
                String description
        ) {
        }

        @Schema(name = "값 증감 요청")
        public record ValueUpdate(
                @NotNull
                @Schema(description = "+1이면 증가, -1이면 감소", example = "1")
                Integer changedValue
        ) {}
}
