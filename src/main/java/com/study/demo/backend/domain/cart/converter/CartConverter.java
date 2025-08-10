package com.study.demo.backend.domain.cart.converter;

import com.study.demo.backend.domain.cart.dto.response.CartResDTO;
import com.study.demo.backend.domain.cart.entity.Cart;
import com.study.demo.backend.domain.cart.entity.CartMenu;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartConverter {

    public static CartResDTO.AddMenu toAddMenuResponse(Cart cart, List<CartMenu> cartMenus) {
        List<CartResDTO.CartMenuInfo> cartMenuInfoList = cartMenus.stream()
                .map(cm -> new CartResDTO.CartMenuInfo(
                        cm.getMenu().getStore().getId(),
                        cm.getMenu().getId(),
                        cm.getMenu().getName(),
                        cm.getQuantity()
                ))
                .collect(Collectors.toList());

        return CartResDTO.AddMenu.builder()
                .cartId(cart.getId())
                .cartMenuInfoList(cartMenuInfoList)
                .build();
    }
}
