package com.study.demo.backend.domain.store.service.query;

import com.study.demo.backend.domain.store.dto.response.StoreResDTO;
import com.study.demo.backend.domain.store.entity.enums.StoreSortType;
import org.springframework.stereotype.Service;

@Service
public interface QueryService {
    StoreResDTO.StoreDetailList getStoreList(Long cursor, int size, StoreSortType type, Double lat, Double lng);
}
