package com.study.demo.backend.domain.order.service.query;

import com.study.demo.backend.domain.menu.converter.MenuConverter;
import com.study.demo.backend.domain.menu.dto.response.MenuResDTO;
import com.study.demo.backend.domain.menu.entity.Menu;
import com.study.demo.backend.domain.menu.entity.menuEnums.MenuSortType;
import com.study.demo.backend.domain.menu.exception.MenuErrorCode;
import com.study.demo.backend.domain.menu.exception.MenuException;
import com.study.demo.backend.domain.menu.repository.MenuRepository;
import com.study.demo.backend.domain.order.converter.OrderConverter;
import com.study.demo.backend.domain.order.dto.response.OrderResDTO;
import com.study.demo.backend.domain.order.entity.Order;
import com.study.demo.backend.domain.order.repository.OrderRepository;
import com.study.demo.backend.domain.store.exception.StoreErrorCode;
import com.study.demo.backend.domain.store.exception.StoreException;
import com.study.demo.backend.domain.store.repository.StoreRepository;
import com.study.demo.backend.domain.user.entity.enums.Role;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderRepository orderRepository;

    @Override
    public OrderResDTO.OrderList getUserOrders( Long cursor, int size) {
        List<Order> orderList = orderRepository.findAllOrders(cursor, size);

        List<OrderResDTO.OrderDetail> orderDetailList = orderList.stream()
                .map(OrderConverter::toOrderDetail)
                .collect(Collectors.toList());

        Long nextCursor = -1L;
        if (!orderDetailList.isEmpty()) {
            nextCursor = orderDetailList.get(orderDetailList.size() - 1).orderId();
            if (orderDetailList.size() < size) {
                nextCursor = -1L;
            }
        }

        return OrderConverter.toOrderList(orderDetailList, nextCursor);
    }
}