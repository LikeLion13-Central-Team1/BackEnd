package com.study.demo.backend.domain.store.service.query;

import com.study.demo.backend.domain.store.dto.response.StoreResDTO;
import com.study.demo.backend.domain.store.entity.storeEnums.StoreSortType;
import org.springframework.stereotype.Service;

@Service
public interface StoreQueryService {
    StoreResDTO.StoreDetailList getStoreList(Long cursor, int size, StoreSortType type, double lat, double lng, Long userId);
    StoreResDTO.StoreDetail getStoreDetail(Long storeId, Long userId);
}
