package com.study.demo.backend.domain.order.controller;

import com.study.demo.backend.domain.order.dto.request.OrderReqDTO;
import com.study.demo.backend.domain.order.dto.response.OrderResDTO;
import com.study.demo.backend.domain.order.exception.OrderErrorCode;
import com.study.demo.backend.domain.order.service.command.OrderCommandService;
import com.study.demo.backend.domain.order.service.query.OrderQueryService;
import com.study.demo.backend.domain.user.entity.enums.Role;
import com.study.demo.backend.global.apiPayload.CustomResponse;
import com.study.demo.backend.global.apiPayload.exception.CustomException;
import com.study.demo.backend.global.security.annotation.CurrentUser;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "주문 API", description = "주문 관련 API")
@RequiredArgsConstructor
public class OrderController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    @PostMapping("/stores/{storeId}/orders")
    @Operation(summary = "주문 생성 API", description = "사용자의 Role이 CUSTOMER인 경우 주문을 생성합니다.")
    public CustomResponse<OrderResDTO.CreateOrder> createOrder(
            @Parameter(description = "주문할 가게 ID") @PathVariable Long storeId,
            @Valid @RequestBody OrderReqDTO.CreateOrder request,
            @CurrentUser AuthUser authUser
    ) {
        if (!authUser.getRole().equals(Role.CUSTOMER)) { // 손님일 경우만 주문 할 수 있도록
            throw new CustomException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }

        OrderResDTO.CreateOrder response = orderCommandService.createOrder(storeId, request, authUser);
        return CustomResponse.onSuccess(response);
    }

    @PostMapping("/orders")
    @Operation(summary = "주문 생성 API by 김지명", description = "장바구니를 기반으로 주문을 생성합니다. 주문 완료 후 장바구니는 자동으로 비워집니다.")
    public CustomResponse<OrderResDTO.CreateOrder> createOrder(
            @Valid @RequestBody OrderReqDTO.CreateOrderByCartId request,
            @CurrentUser AuthUser authUser
    ) {
        if (!authUser.getRole().equals(Role.CUSTOMER)) {
            throw new CustomException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }

        OrderResDTO.CreateOrder response = orderCommandService.createOrderFromCart(request, authUser);
        return CustomResponse.onSuccess(response);
    }

    @GetMapping("/orders")
    @Operation(summary = "주문 목록 조회 API by 최현우", description = """
            주문 내역을 커서 기반 페이지네이션으로 조회합니다. CUSTOMER와 OWNER 모두 이 기능을 사용할 수 있도록 하였습니다.
            - 정렬 기준 : 항상 최신순으로 정렬됩니다.
            - cursor : 마지막으로 조회한 orderId 
            - size : 한 번에 조회할 데이터 수 (기본 10개)""")
    public CustomResponse<OrderResDTO.OrderList> getOrderList(
            @Parameter(description = "마지막으로 조회한 주문 ID (커서)")
            @RequestParam(required = false) Long cursor,

            @Parameter(description = "한 번에 조회할 항목 수")
            @RequestParam(defaultValue = "10") int size
    ) {
        OrderResDTO.OrderList orderHistoryList = orderQueryService.getUserOrders( cursor, size);
        return CustomResponse.onSuccess(orderHistoryList);
    }
}