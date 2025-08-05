package com.study.demo.backend.domain.menu.entity;

import com.study.demo.backend.domain.store.entity.Store;
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

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "discount_percent", nullable = false)
    private int discountPercent;

    @Column(name = "discount_price", nullable = false)
    private BigDecimal discountPrice;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "menu_image")
    private String menuImage;
}
