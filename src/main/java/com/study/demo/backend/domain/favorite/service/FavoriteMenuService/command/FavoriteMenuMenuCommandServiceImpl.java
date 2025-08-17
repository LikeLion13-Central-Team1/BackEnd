package com.study.demo.backend.domain.favorite.service.FavoriteMenuService.command;

import com.study.demo.backend.domain.favorite.entity.favoriteMenu.FavoriteMenu;
import com.study.demo.backend.domain.favorite.exception.FavoriteErrorCode;
import com.study.demo.backend.domain.favorite.repository.FavoriteMenuRepository;
import com.study.demo.backend.domain.menu.entity.Menu;
import com.study.demo.backend.domain.menu.exception.MenuErrorCode;
import com.study.demo.backend.domain.menu.repository.MenuRepository;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.domain.user.exception.UserErrorCode;
import com.study.demo.backend.domain.user.repository.UserRepository;
import com.study.demo.backend.global.apiPayload.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteMenuMenuCommandServiceImpl implements FavoriteMenuCommandService {

    private final FavoriteMenuRepository favoriteMenuRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    @Override
    public void create(Long userId, Long menuId) {
        if (favoriteMenuRepository.existsByUserIdAndMenuId(userId, menuId)){
            throw new CustomException(FavoriteErrorCode.ALREADY_FAVORITED);
        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(MenuErrorCode.MENU_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        try {
            favoriteMenuRepository.save(FavoriteMenu.of(user, menu));
        } catch (DataIntegrityViolationException ignore) {}
    }

    @Override
    public void delete(Long userId, Long menuId) {
        favoriteMenuRepository.findByUserIdAndMenuId(userId, menuId)
                .ifPresent(favoriteMenuRepository::delete);
    }
}