package com.study.demo.backend.domain.cart.service.command;

import com.study.demo.backend.domain.cart.converter.CartConverter;
import com.study.demo.backend.domain.cart.dto.requset.CartReqDTO;
import com.study.demo.backend.domain.cart.dto.response.CartResDTO;
import com.study.demo.backend.domain.cart.entity.Cart;
import com.study.demo.backend.domain.cart.entity.CartMenu;
import com.study.demo.backend.domain.cart.exception.CartErrorCode;
import com.study.demo.backend.domain.cart.exception.CartException;
import com.study.demo.backend.domain.cart.repository.CartMenuRepository;
import com.study.demo.backend.domain.cart.repository.CartRepository;
import com.study.demo.backend.domain.menu.entity.Menu;
import com.study.demo.backend.domain.menu.repository.MenuRepository;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.domain.user.exception.UserErrorCode;
import com.study.demo.backend.domain.user.exception.UserException;
import com.study.demo.backend.domain.user.repository.UserRepository;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartCommandServiceImpl implements CartCommandService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final CartMenuRepository cartMenuRepository;

    @Override
    @Transactional
    public CartResDTO.AddMenu addMenuToCart(CartReqDTO.AddMenu reqDTO, AuthUser authUser) {
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(
                        Cart.builder().user(user).build()
                ));

        Menu menu = menuRepository.findById(reqDTO.menuId())
                .orElseThrow(() -> new CartException(CartErrorCode.MENU_NOT_FOUND));

        Long newStoreId = menu.getStore().getId();

        // 기존 카트의 가게와 비교
        List<Long> storeIds = cartMenuRepository.findDistinctStoreIdsByCartId(cart.getId());
        if (!storeIds.isEmpty() && (storeIds.size() > 1 || !storeIds.get(0).equals(newStoreId))) {
            cartMenuRepository.deleteByCartId(cart.getId()); // 비우기
        }

        // 동일 메뉴 있으면 가져오고, 없으면 생성(초기 quantity=0)
        CartMenu cartMenu = cartMenuRepository.findByCartIdAndMenuId(cart.getId(), menu.getId())
                .orElseGet(() -> cartMenuRepository.save(
                        CartMenu.builder()
                                .cart(cart)
                                .menu(menu)
                                .quantity(0)
                                .build()
                ));

        // 수량 "설정"
        cartMenu.setQuantity(reqDTO.quantity());

        List<CartMenu> items = cartMenuRepository.findByCartId(cart.getId());
        return CartConverter.toAddMenuResponse(cart, items);
    }
}
