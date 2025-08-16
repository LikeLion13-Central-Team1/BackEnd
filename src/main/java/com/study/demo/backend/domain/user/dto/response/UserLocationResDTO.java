package com.study.demo.backend.domain.user.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

public class UserLocationResDTO {

    @Builder
    public record LocationInfo(
            Long locationId,
            String locationName,
            String roadAddressName,
            BigDecimal latitude,
            BigDecimal longitude,
            boolean active
    ) {
    }

    public record LocationInfoList(
            List<LocationInfo> locationInfoList
    ) {
    }
}
