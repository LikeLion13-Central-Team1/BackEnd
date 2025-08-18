package com.study.demo.backend.domain.favorite.converter;

import com.study.demo.backend.domain.favorite.dto.response.FavoriteResDTO;
import com.study.demo.backend.domain.favorite.entity.favoriteMenu.FavoriteMenu;
import com.study.demo.backend.domain.favorite.entity.favoriteStore.FavoriteStore;
import com.study.demo.backend.domain.menu.entity.Menu;
import com.study.demo.backend.domain.store.entity.Store;

import java.util.List;

public class FavoriteConverter {
    public static FavoriteResDTO.FavoriteMenu toFavoriteMenu(FavoriteMenu favoriteMenu) {
        Menu menu = favoriteMenu.getMenu();
        Store store = menu.getStore();

        return FavoriteResDTO.FavoriteMenu.builder()
                .favoriteId(favoriteMenu.getId())
                .menuId(menu.getId())
                .menuName(menu.getName())
                .storeId(store.getId())
                .storeName(store.getName())
                .price(menu.getPrice())
                .discountPercent(menu.getDiscountPercent())
                .discountPrice(menu.getDiscountPrice())
                .menuImage(menu.getMenuImage())
                .createdAt(favoriteMenu.getCreatedAt())
                .build();
    }

    public static FavoriteResDTO.FavoriteMenuList toFavoriteMenuList(
            List<FavoriteResDTO.FavoriteMenu> items,
            Long nextCursor,
            boolean hasData
    ) {
        return FavoriteResDTO.FavoriteMenuList.builder()
                .FavoriteMenus(items)
                .nextCursor(nextCursor)
                .hasData(hasData)
                .build();
    }

    public static FavoriteResDTO.FavoriteMenuList toFavoriteMenuListFromEntities(
            List<FavoriteMenu> entities,
            Long nextCursor,
            boolean hasData
    ) {
        List<FavoriteResDTO.FavoriteMenu> items = entities.stream()
                .map(FavoriteConverter::toFavoriteMenu)
                .toList();

        return toFavoriteMenuList(items, nextCursor, hasData);
    }

    // ====== 가게 찜 ======
    public static FavoriteResDTO.FavoriteStore toFavoriteStore(FavoriteStore favoriteStore) {
        Store store = favoriteStore.getStore();

        return FavoriteResDTO.FavoriteStore.builder()
                .favoriteId(favoriteStore.getId())
                .storeId(store.getId())
                .storeName(store.getName())
                .storeImage(store.getImageUrl())
                .createdAt(favoriteStore.getCreatedAt())
                .build();
    }

    public static FavoriteResDTO.FavoriteStoreList toFavoriteStoreList(
            List<FavoriteResDTO.FavoriteStore> items,
            Long nextCursor,
            boolean hasData
    ) {
        return FavoriteResDTO.FavoriteStoreList.builder()
                .FavoriteStores(items)
                .nextCursor(nextCursor)
                .hasData(hasData)
                .build();
    }

    public static FavoriteResDTO.FavoriteStoreList toFavoriteStoreListFromEntities(
            List<FavoriteStore> entities,
            Long nextCursor,
            boolean hasData
    ) {
        List<FavoriteResDTO.FavoriteStore> items = entities.stream()
                .map(FavoriteConverter::toFavoriteStore)
                .toList();

        return toFavoriteStoreList(items, nextCursor, hasData);
    }
}
