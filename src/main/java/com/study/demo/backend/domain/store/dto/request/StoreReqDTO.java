package com.study.demo.backend.domain.store.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalTime;

public class StoreReqDTO {

    public record CreateStoreReq(
            @NotBlank(message = "가게 이름은 필수입니다.")
            @Schema(description = "가게 이름", example = "상명김밥")
            String name,

            @NotNull(message = "위도는 필수입니다.")
            @Schema(description = "위도 (latitude), 범위: -90 ~ 90", example = "37.6005")
            BigDecimal latitude,

            @NotNull(message = "경도는 필수입니다.")
            @Schema(description = "경도 (longitude), 범위: -180 ~ 180", example = "126.9645")
            BigDecimal longitude,

            @NotNull(message = "오픈시간은 필수입니다.")
            @Schema(description = "오픈 시간 (HH:mm:ss)", example = "08:00:00")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
            LocalTime openingTime,

            @NotNull(message = "마감시간은 필수입니다.")
            @Schema(description = "마감 시간 (HH:mm:ss)", example = "20:00:00")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
            LocalTime closingTime
    ) { }

    @Builder
    public record UpdateStoreReq(
            Long storeId,

            @Schema(description = "가게 이름", example = "가게 이름")
            String name,

            @Schema(description = "오픈 시간 (HH:mm:ss)", example = "08:00:00")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
            LocalTime openingTime,

            @Schema(description = "마감 시간 (HH:mm:ss)", example = "22:00:00")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
            LocalTime closingTime,

            @Schema(description = "위도 (latitude, -90 ~ 90)", example = "37.6005")
            BigDecimal latitude,

            @Schema(description = "경도 (longitude, -180 ~ 180)", example = "126.9645")
            BigDecimal longitude
    ) {}
}
