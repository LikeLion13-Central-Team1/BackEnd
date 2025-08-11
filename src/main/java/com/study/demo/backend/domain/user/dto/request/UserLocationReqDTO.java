package com.study.demo.backend.domain.user.dto.request;

import java.math.BigDecimal;

public class UserLocationReqDTO {

    public record Create(
            BigDecimal latitude,
            BigDecimal longitude,
            String name
    ) {
    }

    public record Modify(
            BigDecimal latitude,
            BigDecimal longitude,
            String name
    ) {
    }
}
