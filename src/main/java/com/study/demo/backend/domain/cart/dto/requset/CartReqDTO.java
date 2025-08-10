package com.study.demo.backend.domain.cart.dto.requset;

public class CartReqDTO {

    public record AddMenu(
            Long menuId,
            int quantity
    ) {
    }
}
