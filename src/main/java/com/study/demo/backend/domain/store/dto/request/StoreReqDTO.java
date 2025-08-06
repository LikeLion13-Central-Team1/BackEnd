package com.study.demo.backend.domain.store.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalTime;

public class StoreReqDTO {

    public record Create(
            @NotBlank(message = "가게 이름은 필수입니다.")
            String name,

            @NotNull(message = "위도는 필수입니다.")
            Double latitude,

            @NotNull(message = "경도는 필수입니다.")
            Double longitude,

            @Schema(description = "오픈 시간 (HH:mm:ss)", example = "08:00:00")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
            LocalTime openingTime,

            @Schema(description = "마감 시간 (HH:mm:ss)", example = "20:00:00")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
            LocalTime closingTime
    ) {
    }

    @Builder
    public record Update(
            Long storeId,

            @Schema(description = "가게 이름", example = "가게 이름")
            String name,

            @Schema(description = "오픈 시간 (HH:mm:ss)", example = "08:00:00")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
            LocalTime openingTime,

            @Schema(description = "마감 시간 (HH:mm:ss)", example = "22:00:00")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
            LocalTime closingTime,

            @Schema(description = "위도", example = "37.6005")
            BigDecimal latitude,

            @Schema(description = "경도", example = "126.9645")
            BigDecimal longitude
    ) {}
}
