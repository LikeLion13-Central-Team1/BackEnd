package com.study.demo.backend.domain.order.service.command;

import com.study.demo.backend.domain.order.dto.request.OrderReqDTO;
import com.study.demo.backend.domain.order.dto.response.OrderResDTO;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import org.springframework.stereotype.Service;

@Service
public interface OrderCommandService {
    OrderResDTO.CreateOrder createOrder(Long storeId, OrderReqDTO.CreateOrder request,AuthUser authUser);
}