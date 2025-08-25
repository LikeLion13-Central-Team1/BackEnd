package com.study.demo.backend.domain.order.service.command;

import com.study.demo.backend.domain.cart.entity.Cart;
import com.study.demo.backend.domain.cart.entity.CartMenu;
import com.study.demo.backend.domain.cart.exception.CartErrorCode;
import com.study.demo.backend.domain.cart.exception.CartException;
import com.study.demo.backend.domain.cart.repository.CartMenuRepository;
import com.study.demo.backend.domain.cart.repository.CartRepository;
import com.study.demo.backend.domain.menu.entity.Menu;
import com.study.demo.backend.domain.menu.exception.MenuErrorCode;
import com.study.demo.backend.domain.menu.repository.MenuRepository;
import com.study.demo.backend.domain.order.converter.OrderConverter;
import com.study.demo.backend.domain.order.dto.request.OrderReqDTO;
import com.study.demo.backend.domain.order.dto.response.OrderResDTO;
import com.study.demo.backend.domain.order.entity.Order;
import com.study.demo.backend.domain.order.entity.OrderMenu;
import com.study.demo.backend.domain.order.exception.OrderErrorCode;
import com.study.demo.backend.domain.order.repository.OrderRepository;
import com.study.demo.backend.domain.store.entity.Store;
import com.study.demo.backend.domain.store.exception.StoreErrorCode;
import com.study.demo.backend.domain.store.repository.StoreRepository;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.domain.user.exception.UserErrorCode;
import com.study.demo.backend.domain.user.repository.UserRepository;
import com.study.demo.backend.global.apiPayload.exception.CustomException;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final CartRepository cartRepository;
    private final CartMenuRepository cartMenuRepository;

    @Override
    public OrderResDTO.CreateOrder createOrder(Long storeId, OrderReqDTO.CreateOrder createOrder, AuthUser authUser) {
        if (createOrder.visitTime().isBefore(LocalDateTime.now())) {
            throw new CustomException(OrderErrorCode.VISIT_TIME_ERROR);
        }

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(StoreErrorCode.STORE_NOT_FOUND));

        // 마감 10시간 전 ~ 마감 시간까지 -> 주문 생성 가능 시간 확인
        LocalTime closingTime = store.getClosingTime();
        LocalDate today = LocalDate.now();
        LocalDateTime todayClosingTime = LocalDateTime.of(today, closingTime);
        LocalDateTime orderAvailableStartTime = todayClosingTime.minusHours(10);

        LocalDateTime now = LocalDateTime.now();
//        if (now.isBefore(orderAvailableStartTime) || now.isAfter(todayClosingTime)) {
//            throw new CustomException(OrderErrorCode.ORDER_TIME_UNAVAILABLE);
//        } 주문 생성 제한 없애기!

        List<Long> menuIdList = createOrder.orderMenus().stream()
                .map(OrderReqDTO.OrderItem::menuId)
                .toList();

        Map<Long, Menu> menuMap = menuRepository.findAllById(menuIdList).stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu));

        if (menuIdList.size() != menuMap.size()) {
            throw new CustomException(OrderErrorCode.MENU_NOT_FOUND_IN_ORDER);
        }

        List<OrderMenu> orderMenus = createOrder.orderMenus().stream().map(orderItem -> {
            Menu menu = menuMap.get(orderItem.menuId());

            if (!menu.getStore().getId().equals(storeId)) {
                throw new CustomException(MenuErrorCode.MENU_STORE_MISMATCH);
            }
            if (menu.getQuantity() < orderItem.quantity()) {
                throw new CustomException(OrderErrorCode.INSUFFICIENT_STOCK);
            }

            menu.updateQuantity(menu.getQuantity() - orderItem.quantity()); // 재고의 수량 줄이기
            return OrderConverter.toOrderMenu(menu, orderItem.quantity());
        }).toList();

        // 전체 가격 계산하기
        BigDecimal totalPrice = orderMenus.stream()
                .map(orderMenu -> {
                    BigDecimal price = orderMenu.getMenu().getDiscountPrice();
                    BigDecimal quantity = BigDecimal.valueOf(orderMenu.getQuantity());
                    return price.multiply(quantity);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order newOrder = OrderConverter.toOrder(user, store, createOrder, totalPrice);

        orderMenus.forEach(orderMenu -> newOrder.addOrderMenu(orderMenu));

        orderRepository.save(newOrder);

        return OrderConverter.toCreateOrderResponse(newOrder);
    }

    @Override
    public OrderResDTO.CreateOrder createOrderFromCart(OrderReqDTO.CreateOrderByCartId request, AuthUser authUser) {
        // UTC 기준으로 통일
        if (request.visitTime().isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
            throw new CustomException(OrderErrorCode.VISIT_TIME_ERROR);
        }

        // 사용자 조회
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        // 장바구니 조회
        Cart cart = cartRepository.findById(request.cartId())
                .orElseThrow(() -> new CartException(CartErrorCode.CART_NOT_FOUND));

        // 장바구니 소유자 확인
        if (!cart.getUser().getId().equals(user.getId())) {
            throw new CustomException(OrderErrorCode.CART_ACCESS_DENIED);
        }

        // 장바구니 메뉴 조회 (메뉴와 가게 정보도 함께 조회)
        List<CartMenu> cartMenus = cartMenuRepository.findByCartIdWithMenuAndStore(cart.getId());

        if (cartMenus.isEmpty()) {
            throw new CartException(CartErrorCode.CART_EMPTY);
        }

        // 모든 메뉴가 같은 가게인지 확인 (장바구니 로직에서 이미 보장되지만 한번 더 체크)
        Store store = cartMenus.get(0).getMenu().getStore();
        boolean isSameStore = cartMenus.stream()
                .allMatch(cm -> cm.getMenu().getStore().getId().equals(store.getId()));

        if (!isSameStore) {
            throw new CustomException(OrderErrorCode.DIFFERENT_STORE_MENUS);
        }

        // 마감 3시간 전 ~ 마감 시간까지 -> 주문 생성 가능 시간 확인
        LocalTime closingTime = store.getClosingTime();
        LocalDate today = LocalDate.now();
        LocalDateTime todayClosingTime = LocalDateTime.of(today, closingTime);
        LocalDateTime orderAvailableStartTime = todayClosingTime.minusHours(3);

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(orderAvailableStartTime) || now.isAfter(todayClosingTime)) {
            throw new CustomException(OrderErrorCode.ORDER_TIME_UNAVAILABLE);
        }

        // 재고 확인 및 차감, OrderMenu 생성
        List<OrderMenu> orderMenus = cartMenus.stream().map(cartMenu -> {
            Menu menu = cartMenu.getMenu();
            int orderQuantity = cartMenu.getQuantity();

            // 재고 확인
            if (menu.getQuantity() < orderQuantity) {
                throw new CustomException(OrderErrorCode.INSUFFICIENT_STOCK);
            }

            // 재고 차감
            menu.updateQuantity(menu.getQuantity() - orderQuantity);

            // OrderMenu 생성
            return OrderConverter.toOrderMenu(menu, orderQuantity);
        }).toList();

        // 총 가격 계산
        BigDecimal totalPrice = orderMenus.stream()
                .map(orderMenu -> {
                    BigDecimal price = orderMenu.getMenu().getDiscountPrice();
                    BigDecimal quantity = BigDecimal.valueOf(orderMenu.getQuantity());
                    return price.multiply(quantity);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 주문 생성
        Order newOrder = OrderConverter.toOrder(user, store, request, totalPrice);

        // OrderMenu들을 주문에 추가
        orderMenus.forEach(orderMenu -> newOrder.addOrderMenu(orderMenu));

        // 주문 저장
        orderRepository.save(newOrder);

        // 장바구니 비우기 (주문 완료 후)
        cartMenuRepository.deleteByCartId(cart.getId());

        return OrderConverter.toCreateOrderResponse(newOrder);
    }
}
