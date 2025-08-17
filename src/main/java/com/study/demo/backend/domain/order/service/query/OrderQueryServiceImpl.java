package com.study.demo.backend.domain.order.service.query;

import com.study.demo.backend.domain.order.converter.OrderConverter;
import com.study.demo.backend.domain.order.dto.response.OrderResDTO;
import com.study.demo.backend.domain.order.entity.Order;
import com.study.demo.backend.domain.order.repository.OrderRepository;
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