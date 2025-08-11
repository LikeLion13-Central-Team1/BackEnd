package com.study.demo.backend.domain.menu.converter;

import com.study.demo.backend.domain.menu.dto.request.MenuReqDTO;
import com.study.demo.backend.domain.menu.dto.response.MenuResDTO;
import com.study.demo.backend.domain.menu.entity.Menu;
import com.study.demo.backend.domain.store.entity.Store;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MenuConverter {

    public static Menu toEntity(MenuReqDTO.CreateMenu dto, String imageUrl, Store store) {
        Integer percent = dto.discountPercent() == null ? 0 : dto.discountPercent();
        return Menu.builder()
                .name(dto.name())
                .price(dto.price())
                .discountPercent(percent)
                .discountPrice(BigDecimal.ZERO)
                .description(dto.description())
                .menuImage(imageUrl)
                .store(store)
                .quantity(dto.quantity())
                .build();
    }

    public static MenuResDTO.CreateMenu toMenuCreateRes(Menu menu) {
        return MenuResDTO.CreateMenu.builder()
                .menuId(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .discountPercent(menu.getDiscountPercent())
                .discountPrice(menu.getDiscountPrice())
                .quantity(menu.getQuantity())
                .description(menu.getDescription())
                .menuImage(menu.getMenuImage())
                .build();
    }

    public static MenuResDTO.MenuDetail toDetail(Menu m) {
        return MenuResDTO.MenuDetail.builder()
                .menuId(m.getId())
                .name(m.getName())
                .price(m.getPrice())
                .discountPercent(m.getDiscountPercent())
                .description(m.getDescription())
                .menuImage(m.getMenuImage())
                .quantity(m.getQuantity())
                .build();
    }

    public static MenuResDTO.UpdateMenu toMenuUpdateRes(Menu m) {
        return MenuResDTO.UpdateMenu.builder()
                .menuId(m.getId())
                .name(m.getName())
                .price(m.getPrice())
                .discountPercent(m.getDiscountPercent())
                .discountPrice(m.getDiscountPrice())
                .quantity(m.getQuantity())
                .description(m.getDescription())
                .menuImage(m.getMenuImage())
                .build();
    }
}
