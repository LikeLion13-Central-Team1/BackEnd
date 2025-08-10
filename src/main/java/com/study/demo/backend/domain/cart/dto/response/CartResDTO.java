package com.study.demo.backend.domain.cart.dto.response;

import lombok.Builder;

import java.util.List;

public class CartResDTO {

    @Builder
    public record AddMenu(
            Long cartId,
            List<CartMenuInfo> cartMenuInfoList
    ) {
    }

    public record CartMenuInfo(
            Long storeId,
            Long menuId,
            String menuName,
            int quantity
    ) {
    }
}
