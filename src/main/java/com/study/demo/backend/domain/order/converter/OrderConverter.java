package com.study.demo.backend.domain.order.converter;

import com.study.demo.backend.domain.menu.entity.Menu;
import com.study.demo.backend.domain.order.dto.request.OrderReqDTO;
import com.study.demo.backend.domain.order.dto.response.OrderResDTO;
import com.study.demo.backend.domain.order.entity.Order;
import com.study.demo.backend.domain.order.entity.OrderMenu;
import com.study.demo.backend.domain.store.entity.Store;
import com.study.demo.backend.domain.user.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderConverter {
    public static Order toOrder(User user, Store store, OrderReqDTO.CreateOrder request, BigDecimal totalPrice) {
        return Order.builder()
                .user(user)
                .store(store)
                .orderTime(LocalDateTime.now())
                .visitTime(request.visitTime())
                .totalPrice(totalPrice)
                .orderNum(UUID.randomUUID().toString())
                .orderMenus(new ArrayList<>())
                .build();
    }

    // CartId로 픽업 예약 생성 전용
    public static Order toOrder(User user, Store store, OrderReqDTO.CreateOrderByCartId request, BigDecimal totalPrice) {
        return Order.builder()
                .user(user)
                .store(store)
                .orderTime(LocalDateTime.now())
                .visitTime(request.visitTime())
                .totalPrice(totalPrice)
                .orderNum(generateRandomOrderNum())
                .orderMenus(new ArrayList<>())
                .build();
    }

    private static String generateRandomOrderNum() {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder(5);

        for (int i = 0; i < 5; i++) {
            result.append(chars.charAt(random.nextInt(chars.length())));
        }

        return result.toString();
    }

    public static OrderMenu toOrderMenu(Menu menu, int quantity) {
        return OrderMenu.builder()
                .menu(menu)
                .quantity(quantity)
                .build();
    }

    public static OrderResDTO.CreateOrder toCreateOrderResponse(Order order) {
        return OrderResDTO.CreateOrder.builder()
                .orderId(order.getId())
                .orderNum(order.getOrderNum())
                .orderTime(order.getOrderTime())
                .visitTime(order.getVisitTime())
                .totalPrice(order.getTotalPrice())
                .storeName(order.getStore().getName())
                .build();
    }

    public static OrderResDTO.OrderDetail toOrderDetail(Order order) {
        List<String> menuAndQuantity = order.getOrderMenus().stream()
                .map(orderMenu -> orderMenu.getMenu().getName() + " X " + orderMenu.getQuantity())
                .collect(Collectors.toList());

        return OrderResDTO.OrderDetail.builder()
                .orderId(order.getId())
                .orderNum(order.getOrderNum())
                .storeName(order.getStore().getName())
                .totalPrice(order.getTotalPrice())
                .orderTime(order.getOrderTime())
                .menuSummaries(menuAndQuantity)
                .build();
    }

    public static OrderResDTO.OrderList toOrderList(List<OrderResDTO.OrderDetail> orderList, Long nextCursor) {
        return OrderResDTO.OrderList.builder()
                .orderList(orderList)
                .nextCursor(nextCursor)
                .build();
    }
}
