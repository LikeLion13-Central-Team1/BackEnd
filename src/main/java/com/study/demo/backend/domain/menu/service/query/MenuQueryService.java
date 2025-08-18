package com.study.demo.backend.domain.menu.service.query;

import com.study.demo.backend.domain.menu.dto.response.MenuResDTO;
import com.study.demo.backend.domain.menu.entity.menuEnums.MenuSortType;
import org.springframework.stereotype.Service;

@Service
public interface MenuQueryService {
    MenuResDTO.MenuDetailList getStoreMenus(Long storeId, Long cursor, int size, MenuSortType type, Long userId);

    MenuResDTO.MenuDetailList getMenus(Long cursor, int size, MenuSortType sortType, Long userId);

    MenuResDTO.MenuDetail getMenuDetail(Long storeId, Long menuId, Long userId);
}
