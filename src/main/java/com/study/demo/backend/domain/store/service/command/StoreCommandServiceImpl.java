package com.study.demo.backend.domain.store.service.command;

import com.study.demo.backend.domain.store.converter.StoreConverter;
import com.study.demo.backend.domain.store.dto.request.StoreReqDTO;
import com.study.demo.backend.domain.store.dto.response.StoreResDTO;
import com.study.demo.backend.domain.store.entity.Store;
import com.study.demo.backend.domain.store.exception.StoreErrorCode;
import com.study.demo.backend.domain.store.exception.StoreException;
import com.study.demo.backend.domain.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StoreCommandServiceImpl implements StoreCommandService {

    private final StoreRepository storeRepository;

    @Override
    public StoreResDTO.CreateStoreReq createStore(StoreReqDTO.@Valid CreateStoreReq create) {
        if (storeRepository.existsByName(create.name())) {
            throw new StoreException(StoreErrorCode.STORE_ALREADY_EXISTS);
        }

        if (create.latitude().compareTo(new BigDecimal("-90")) < 0 || create.latitude().compareTo(new BigDecimal("90")) > 0) {
            throw new StoreException(StoreErrorCode.INVALID_LOCATION);
        }

        if (create.longitude().compareTo(new BigDecimal("-180")) < 0 || create.longitude().compareTo(new BigDecimal("180")) > 0) {
            throw new StoreException(StoreErrorCode.INVALID_LOCATION);
        }

        Store store = StoreConverter.toEntity(create);
        storeRepository.save(store);

        return StoreConverter.toCreateDTO(store);
    }

    @Override
    public StoreResDTO.UpdateStoreReq updateStore(Long storeId, StoreReqDTO.@Valid UpdateStoreReq updateStoreReqDTO) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        if (updateStoreReqDTO.latitude() != null && (
                updateStoreReqDTO.latitude().compareTo(new BigDecimal("-90")) < 0 ||
                        updateStoreReqDTO.latitude().compareTo(new BigDecimal("90")) > 0)) {
            throw new StoreException(StoreErrorCode.INVALID_LOCATION);
        }

        if (updateStoreReqDTO.longitude() != null && (
                updateStoreReqDTO.longitude().compareTo(new BigDecimal("-180")) < 0 ||
                        updateStoreReqDTO.longitude().compareTo(new BigDecimal("180")) > 0)) {
            throw new StoreException(StoreErrorCode.INVALID_LOCATION);
        }

        store.update(
                updateStoreReqDTO.name(),
                updateStoreReqDTO.latitude(),
                updateStoreReqDTO.longitude(),
                updateStoreReqDTO.openingTime(),
                updateStoreReqDTO.closingTime()
        );

        return StoreConverter.toUpdateDTO(store);
    }
}
