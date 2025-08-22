package com.study.demo.backend.domain.order.service.query;

import com.study.demo.backend.domain.order.converter.OrderConverter;
import com.study.demo.backend.domain.order.dto.response.OrderResDTO;
import com.study.demo.backend.domain.order.entity.Order;
import com.study.demo.backend.domain.order.exception.OrderErrorCode;
import com.study.demo.backend.domain.order.repository.OrderRepository;
import com.study.demo.backend.domain.store.repository.StoreRepository;
import com.study.demo.backend.domain.user.entity.enums.Role;
import com.study.demo.backend.global.apiPayload.exception.CustomException;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    @Override
    public OrderResDTO.OrderList getUserOrders(Long cursor, int size, AuthUser authUser) {
        final int limit = size + 1;
        List<Order> orderList;

        if (authUser.getRole() == Role.CUSTOMER) {
            orderList = orderRepository.findByUserIdWithCursor(authUser.getUserId(), cursor, limit);

        } else if (authUser.getRole() == Role.OWNER) {
            Long storeId = storeRepository.findIdByUserId(authUser.getUserId())
                    .orElseThrow(() -> new CustomException(OrderErrorCode.STORE_NOT_FOUND_FOR_OWNER));
            orderList = orderRepository.findByStoreIdWithCursor(storeId, cursor, limit);

        } else {
            throw new CustomException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }

        boolean hasNext = orderList.size() > size;
        if (hasNext) {
            orderList = orderList.subList(0, size);
        }

        Long nextCursor = orderList.isEmpty() ? -1L : orderList.get(orderList.size() - 1).getId();
        if (!hasNext) nextCursor = -1L;

        return OrderConverter.toOrderList(
                orderList.stream().map(OrderConverter::toOrderDetail).toList(),
                nextCursor
        );
    }
}