package com.study.demo.backend.domain.menu.entity;

import com.study.demo.backend.domain.menu.exception.MenuErrorCode;
import com.study.demo.backend.domain.store.entity.Store;
import com.study.demo.backend.global.apiPayload.exception.CustomException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "name", nullable = false)
    private String name;

    // 정가
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    // 할인율
    @Column(name = "discount_percent", nullable = false)
    private Integer discountPercent;

    // 정가에 할인율이 적용된 가격
    @Column(name = "discount_price", nullable = false)
    private BigDecimal discountPrice;

    @Column(name = "description")
    private String description;

    @Column(name = "menu_image")
    private String menuImage;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // 할인된 가격 구하기
    public void calcDiscountPrice() {
        int percent = (discountPercent == null) ? 0 : discountPercent;
        if (percent < 0 || percent > 100) {
            throw new CustomException(MenuErrorCode.INVALID_DISCOUNT_PERCENT);
        }
        BigDecimal hundred = BigDecimal.valueOf(100);
        BigDecimal p = (price == null) ? BigDecimal.ZERO : price;
        this.discountPrice = p.multiply(hundred.subtract(BigDecimal.valueOf(percent)))
                .divide(hundred, 0, java.math.RoundingMode.HALF_UP);
    }

    // 이름과 설명 업데이트
    public void updateNameDescription(String name, String description) {
        if (name != null && !name.isBlank()) this.name = name.trim();
        if (description != null) this.description = description;
    }

    // 정가, 할인율, 할인된 가격 업데이트
    public void updatePriceDiscount(BigDecimal price, Integer percent) {
        if (price != null) this.price = price;
        if (percent != null) this.discountPercent = percent;
        calcDiscountPrice(); // 항상 재계산
    }

    // 수량 업데이트
    public void updateQuantity(Integer quantity) {
        if (quantity != null) this.quantity = quantity;
    }

    // 이미지 업데이트
    public void updateImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isBlank()) this.menuImage = imageUrl;
    }
}
