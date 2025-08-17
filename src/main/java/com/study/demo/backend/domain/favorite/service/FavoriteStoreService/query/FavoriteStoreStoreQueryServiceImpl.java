package com.study.demo.backend.domain.favorite.service.FavoriteStoreService.query;

import com.study.demo.backend.domain.favorite.converter.FavoriteConverter;
import com.study.demo.backend.domain.favorite.dto.response.FavoriteResDTO;
import com.study.demo.backend.domain.favorite.entity.favoriteStore.FavoriteStore;
import com.study.demo.backend.domain.favorite.repository.FavoriteStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoriteStoreStoreQueryServiceImpl implements FavoriteStoreQueryService {

    private final FavoriteStoreRepository favoriteStoreRepository;

    @Override
    public FavoriteResDTO.FavoriteStoreList findMyFavorites(Long userId, Long cursorId, int size) {
        int pageSize = Math.max(1, Math.min(size, 50));
        int limit = pageSize + 1;

        List<FavoriteStore> rows = favoriteStoreRepository
                .findByUserIdOrderByIdDesc(userId, cursorId, limit);

        boolean hasNext = rows.size() > pageSize;
        List<FavoriteStore> page = hasNext ? rows.subList(0, pageSize) : rows;

        Long nextCursor = page.isEmpty() ? null : page.get(page.size() - 1).getId();

        var items = page.stream()
                .map(FavoriteConverter::toFavoriteStore)
                .toList();

        return new FavoriteResDTO.FavoriteStoreList(items, nextCursor, hasNext);
    }
}