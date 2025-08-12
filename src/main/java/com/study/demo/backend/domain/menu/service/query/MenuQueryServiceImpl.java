package com.study.demo.backend.domain.menu.service.query;

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
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MenuQueryServiceImpl implements MenuQueryService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Override
    public MenuResDTO.MenuDetailList getStoreMenus(Long storeId, Long cursor, int size, MenuSortType type) {
        if (type == null) {
            throw new MenuException(MenuErrorCode.INVALID_SORT_TYPE);
        }

        List<Menu> sorted = switch (type) {
            case PRICE_ASC  -> menuRepository.findMenusByPriceAsc(storeId, cursor, size + 1);
            case PRICE_DESC -> menuRepository.findMenusByPriceDesc(storeId, cursor, size + 1);
            case DISCOUNT   -> menuRepository.findMenusByDiscountDesc(storeId, cursor, size + 1);
        };

        boolean hasNext = sorted.size() > size;

        List<Menu> page = hasNext ? sorted.subList(0, size) : sorted;

        Long nextCursor = page.isEmpty() ? null : page.get(page.size() - 1).getId();

        List<MenuResDTO.MenuDetail> content = page.stream()
                .map(MenuConverter::toDetail).toList();

        return new MenuResDTO.MenuDetailList(content, hasNext, nextCursor);
    }

    @Override
    public MenuResDTO.MenuDetailList getMenus(Long cursor, int size, MenuSortType sortType) {
        if (sortType == null) throw new MenuException(MenuErrorCode.INVALID_SORT_TYPE);

        if (cursor != null && !menuRepository.existsById(cursor)) {
            cursor = null;
        }

        final int limit = size + 1;
        List<Menu> sorted = switch (sortType) {
            case PRICE_ASC  -> menuRepository.findMenusByPriceAsc(null,cursor, limit);
            case PRICE_DESC -> menuRepository.findMenusByPriceDesc(null,cursor, limit);
            case DISCOUNT   -> menuRepository.findMenusByDiscountDesc(null,cursor, limit);
        };

        boolean hasNext = sorted.size() > size;
        List<Menu> page = hasNext ? sorted.subList(0, size) : sorted;

        Long nextCursor = page.isEmpty() ? null : page.get(page.size() - 1).getId();

        var content = page.stream()
                .map(MenuConverter::toDetail)
                .toList();

        return new MenuResDTO.MenuDetailList(content, hasNext, nextCursor);
    }

    @Override
    public MenuResDTO.MenuDetail getMenuDetail(Long storeId, Long menuId) {
        boolean storeExists = storeRepository.existsById(storeId);
        if (!storeExists) {
            throw new StoreException(StoreErrorCode.STORE_NOT_FOUND);
        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(MenuErrorCode.MENU_NOT_FOUND));

        if (!menu.getStore().getId().equals(storeId)) {
            throw new MenuException(MenuErrorCode.MENU_STORE_MISMATCH);
        }

        return MenuConverter.toDetail(menu);
    }
}