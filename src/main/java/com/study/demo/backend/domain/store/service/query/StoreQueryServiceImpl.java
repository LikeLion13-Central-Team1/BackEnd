package com.study.demo.backend.domain.store.service.query;

import com.study.demo.backend.domain.favorite.repository.FavoriteStoreRepository;
import com.study.demo.backend.domain.store.converter.StoreConverter;
import com.study.demo.backend.domain.store.dto.response.StoreResDTO;
import com.study.demo.backend.domain.store.entity.Store;
import com.study.demo.backend.domain.store.entity.storeEnums.StoreSortType;
import com.study.demo.backend.domain.store.exception.StoreErrorCode;
import com.study.demo.backend.domain.store.exception.StoreException;
import com.study.demo.backend.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class StoreQueryServiceImpl implements StoreQueryService {

    private final StoreRepository storeRepository;
    private final FavoriteStoreRepository favoriteStoreRepository;

    @Override
    public StoreResDTO.StoreDetailList getStoreList(Long cursor, int size, StoreSortType type, double lat, double lng, Long userId) {
        final int limit = size + 1;

        if (type == null) {
            throw new StoreException(StoreErrorCode.INVALID_SORT_TYPE);
        }

        List<Store> sorted = switch (type) {
            case DISTANCE -> {
                if (lat < -90.0 || lat > 90.0 || lng < -180.0 || lng > 180.0) {
                    throw new StoreException(StoreErrorCode.INVALID_LOCATION);
                }
                yield storeRepository.findStoresByDistance(cursor, limit, lat, lng);
            }
            case REVIEW -> storeRepository.findStoresByReview(cursor, limit);
            case CREATED_AT -> storeRepository.findStoresByCreatedAt(cursor, limit);
        };

        boolean hasData = sorted.size() > size;
        List<Store> page = hasData ? sorted.subList(0, size) : sorted;

        Set<Long> favoredIds = resolveFavoredIds(userId, page);

        Long nextCursor = page.isEmpty() ? null : page.get(page.size() - 1).getId();

        List<StoreResDTO.StoreDetail> dtoList = page.stream()
                .map(s -> StoreConverter.toStoreDetailDTO(s, favoredIds.contains(s.getId())))
                .toList();

        return new StoreResDTO.StoreDetailList(dtoList, hasData, nextCursor);
    }

    @Override
    public StoreResDTO.StoreDetail getStoreDetail(Long storeId, Long userId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        boolean favorited = favoriteStoreRepository.existsByUserIdAndStoreId(userId, storeId);
        return StoreConverter.toStoreDetailDTO(store, favorited);
    }

    private Set<Long> resolveFavoredIds(Long userId, List<Store> page) {
        if (page.isEmpty()) return Set.of();
        List<Long> storeIds = page.stream().map(Store::getId).toList();
        return new HashSet<>(favoriteStoreRepository.findFavoriteStoreIds(userId, storeIds));
    }

    @Override
    public StoreResDTO.StoreExists checkStoreExists(Long userId) {
        Optional<Long> storeIdOptional = storeRepository.findIdByUserId(userId);

        if (storeIdOptional.isPresent()) {
            Long storeId = storeIdOptional.get();
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

            return StoreResDTO.StoreExists.builder()
                    .exists(true)
                    .storeId(store.getId())
                    .storeName(store.getName())
                    .build();
        } else {
            return StoreResDTO.StoreExists.builder()
                    .exists(false)
                    .storeId(null)
                    .storeName(null)
                    .build();
        }
    }
}
