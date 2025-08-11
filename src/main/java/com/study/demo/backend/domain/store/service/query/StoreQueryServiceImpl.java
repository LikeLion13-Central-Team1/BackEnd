package com.study.demo.backend.domain.store.service.query;

import com.study.demo.backend.domain.store.converter.StoreConverter;
import com.study.demo.backend.domain.store.dto.response.StoreResDTO;
import com.study.demo.backend.domain.store.entity.Store;
import com.study.demo.backend.domain.store.entity.storeEnums.StoreSortType;
import com.study.demo.backend.domain.store.exception.StoreErrorCode;
import com.study.demo.backend.domain.store.exception.StoreException;
import com.study.demo.backend.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StoreQueryServiceImpl implements StoreQueryService {

    private final StoreRepository storeRepository;

    @Override
    public StoreResDTO.StoreDetailList getStoreList(Long cursor, int size, StoreSortType type, Double lat, Double lng) {
        List<Store> stores;

        if (type == null) {
            throw new StoreException(StoreErrorCode.INVALID_SORT_TYPE);
        }

        switch (type) {
            case DISTANCE -> {
                if (lat == null || lng == null) {
                    throw new StoreException(StoreErrorCode.INVALID_LOCATION);
                }
                stores = storeRepository.findStoresByDistance(cursor, size, lat, lng);
            }
            case REVIEW -> stores = storeRepository.findStoresByReview(cursor, size);
            case CREATED_AT -> stores = storeRepository.findStoresByCreatedAt(cursor, size);
            default -> throw new StoreException(StoreErrorCode.INVALID_SORT_TYPE);
        }

        List<StoreResDTO.StoreDetail> dtoList = stores.stream()
                .map(StoreConverter::toStoreDetailDTO)
                .toList();

        Long nextCursor = dtoList.isEmpty() ? null : dtoList.get(dtoList.size() - 1).storeId();
        boolean hasNext = dtoList.size() == size;

        return new StoreResDTO.StoreDetailList(dtoList, hasNext, nextCursor);
    }
}
