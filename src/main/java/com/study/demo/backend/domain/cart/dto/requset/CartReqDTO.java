package com.study.demo.backend.domain.cart.dto.requset;

public class CartReqDTO {

    public record AddMenu(
            Long menuId,
            int quantity
    ) {
    }

    public record UpdateMenuQuantity(
            Long menuId,
            int quantity
    ) {
    }

    public record DeleteMenu(
            Long menuId
    ) {
    }
}
