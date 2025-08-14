package com.study.demo.backend.domain.order.entity;

import com.study.demo.backend.domain.store.entity.Store;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;

    @Column(name = "visit_time", nullable = false)
    private LocalDateTime visitTime;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "order_num", nullable = false)
    private String orderNum;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderMenu> orderMenus = new ArrayList<>();
    public void addOrderMenu(OrderMenu orderMenu) {
        this.orderMenus.add(orderMenu);
        orderMenu.setOrder(this);
    }
}