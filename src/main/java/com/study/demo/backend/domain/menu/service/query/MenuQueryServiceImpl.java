package com.study.demo.backend.domain.menu.service.query;

import com.study.demo.backend.domain.favorite.repository.FavoriteMenuRepository;
import com.study.demo.backend.domain.menu.converter.MenuConverter;
import com.study.demo.backend.domain.menu.dto.response.MenuResDTO;
import com.study.demo.backend.domain.menu.entity.Menu;
import com.study.demo.backend.domain.menu.entity.menuEnums.MenuSortType;
import com.study.demo.backend.domain.menu.exception.MenuErrorCode;
import com.study.demo.backend.domain.menu.exception.MenuException;
import com.study.demo.backend.domain.menu.repository.MenuRepository;
import com.study.demo.backend.domain.store.exception.StoreErrorCode;
import com.study.demo.backend.domain.store.exception.StoreException;
import com.study.demo.backend.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MenuQueryServiceImpl implements MenuQueryService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final FavoriteMenuRepository favoriteMenuRepository;

    @Override
    public MenuResDTO.MenuDetailList getStoreMenus(Long storeId, Long cursor, int size, MenuSortType type,  Long userId) {
        if (type == null) {
            throw new MenuException(MenuErrorCode.INVALID_SORT_TYPE);
        }

        final int fetchSize = size + 1;
        Pageable pageable = PageRequest.of(0, fetchSize);
        List<Menu> sorted = switch (type) {
            case PRICE_ASC  -> menuRepository.findMenusByPriceAsc(storeId, cursor, pageable);
            case PRICE_DESC -> menuRepository.findMenusByPriceDesc(storeId, cursor, pageable);
            case DISCOUNT   -> menuRepository.findMenusByDiscountDesc(storeId, cursor, pageable);
        };

        boolean hasNext = sorted.size() > size;
        List<Menu> page = hasNext ? sorted.subList(0, size) : sorted;
        Set<Long> favoredIds = resolveFavoredIds(userId, page);

        Long nextCursor = page.isEmpty() ? null : page.get(page.size() - 1).getId();
        List<MenuResDTO.MenuDetail> content = page.stream()
                .map(m -> MenuConverter.toDetail(m, favoredIds.contains(m.getId())))
                .toList();

        return new MenuResDTO.MenuDetailList(content, hasNext, nextCursor);
    }

    @Override
    public MenuResDTO.MenuDetailList getMenus(Long cursor, int size, MenuSortType sortType, Long userId) {
        if (sortType == null) {
            throw new MenuException(MenuErrorCode.INVALID_SORT_TYPE);
        }

        if (cursor != null && !menuRepository.existsById(cursor)) {
            cursor = null;
        }

        final int fetchSize = size + 1;
        Pageable pageable = PageRequest.of(0, fetchSize);

        List<Menu> sorted = switch (sortType) {
            case PRICE_ASC  -> menuRepository.findMenusByPriceAsc(null, cursor, pageable);
            case PRICE_DESC -> menuRepository.findMenusByPriceDesc(null, cursor, pageable);
            case DISCOUNT   -> menuRepository.findMenusByDiscountDesc(null, cursor, pageable);
        };

        boolean hasNext = sorted.size() > size;
        List<Menu> page = hasNext ? sorted.subList(0, size) : sorted;

        Set<Long> favoredIds = resolveFavoredIds(userId, page);

        Long nextCursor = page.isEmpty() ? null : page.get(page.size() - 1).getId();
        List<MenuResDTO.MenuDetail> content = page.stream()
                .map(m -> MenuConverter.toDetail(m, favoredIds.contains(m.getId())))
                .toList();

        return new MenuResDTO.MenuDetailList(content, hasNext, nextCursor);
    }

    @Override
    public MenuResDTO.MenuDetail getMenuDetail(Long storeId, Long menuId, Long userId) {
        if (!storeRepository.existsById(storeId)) {
            throw new StoreException(StoreErrorCode.STORE_NOT_FOUND);
        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(MenuErrorCode.MENU_NOT_FOUND));

        if (!menu.getStore().getId().equals(storeId)) {
            throw new MenuException(MenuErrorCode.MENU_STORE_MISMATCH);
        }

        boolean favorited = favoriteMenuRepository.existsByUserIdAndMenuId(userId, menuId);
        return MenuConverter.toDetail(menu, favorited);
    }

    private Set<Long> resolveFavoredIds(Long userId, List<Menu> page) {
        if (page.isEmpty()) return Set.of();

        List<Long> menuIds = page.stream()
                .map(Menu::getId)
                .toList();

        List<Long> favoriteIds = favoriteMenuRepository.findFavoriteMenuIds(userId, menuIds);

        return new HashSet<>(favoriteIds);
    }
}