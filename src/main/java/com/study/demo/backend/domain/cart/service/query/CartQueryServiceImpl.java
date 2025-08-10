package com.study.demo.backend.domain.cart.service.query;

import com.study.demo.backend.domain.cart.converter.CartConverter;
import com.study.demo.backend.domain.cart.dto.response.CartResDTO;
import com.study.demo.backend.domain.cart.entity.Cart;
import com.study.demo.backend.domain.cart.entity.CartMenu;
import com.study.demo.backend.domain.cart.exception.CartErrorCode;
import com.study.demo.backend.domain.cart.exception.CartException;
import com.study.demo.backend.domain.cart.repository.CartMenuRepository;
import com.study.demo.backend.domain.cart.repository.CartRepository;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.domain.user.exception.UserErrorCode;
import com.study.demo.backend.domain.user.exception.UserException;
import com.study.demo.backend.domain.user.repository.UserRepository;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartQueryServiceImpl implements CartQueryService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartMenuRepository cartMenuRepository;

    @Override
    public CartResDTO.CartInfo getCartInfo(AuthUser authUser) {
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CartException(CartErrorCode.CART_NOT_FOUND));

        List<CartMenu> cartMenuList = cartMenuRepository.findByCartIdWithMenuAndStore(cart.getId());

        return CartConverter.toCartInfo(cart, cartMenuList);
    }
}
