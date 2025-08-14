package com.study.demo.backend.domain.order.service.query;

import com.study.demo.backend.domain.menu.dto.response.MenuResDTO;
import com.study.demo.backend.domain.menu.entity.menuEnums.MenuSortType;
import com.study.demo.backend.domain.order.dto.response.OrderResDTO;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import org.springframework.stereotype.Service;

@Service
public interface OrderQueryService {
    OrderResDTO.OrderList getUserOrders(Long cursor, int size);
}
