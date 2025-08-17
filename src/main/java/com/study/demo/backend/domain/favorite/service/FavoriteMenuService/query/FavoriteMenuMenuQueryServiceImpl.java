package com.study.demo.backend.domain.favorite.service.FavoriteMenuService.query;

import com.study.demo.backend.domain.favorite.converter.FavoriteConverter;
import com.study.demo.backend.domain.favorite.dto.response.FavoriteResDTO;
import com.study.demo.backend.domain.favorite.entity.favoriteMenu.FavoriteMenu;
import com.study.demo.backend.domain.favorite.repository.FavoriteMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoriteMenuMenuQueryServiceImpl implements FavoriteMenuQueryService {

    private final FavoriteMenuRepository favoriteMenuRepository;

    @Override
    public FavoriteResDTO.FavoriteMenuList findMyFavorites(Long userId, Long cursorId, int size) {
        int pageSize = Math.max(1, Math.min(size, 50));
        int limit = pageSize + 1;

        List<FavoriteMenu> rows = favoriteMenuRepository
                .findByUserIdOrderByIdDesc(userId, cursorId, limit);

        boolean hasNext = rows.size() > pageSize;
        List<FavoriteMenu> page = hasNext ? rows.subList(0, pageSize) : rows;

        Long nextCursor = page.isEmpty() ? null : page.get(page.size() - 1).getId();

        var items = page.stream()
                .map(FavoriteConverter::toFavoriteMenu)
                .toList();

        return new FavoriteResDTO.FavoriteMenuList(items, nextCursor, hasNext);
    }
}
