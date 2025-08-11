package com.study.demo.backend.domain.user.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

public class UserLocationResDTO {

    @Builder
    public record LocationInfo(
            Long locationId,
            String locationName,
            BigDecimal latitude,
            BigDecimal longitude
    ) {
    }

    public record LocationInfoList(
            List<LocationInfo> locationInfoList
    ) {
    }
}
