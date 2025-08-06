package com.study.demo.backend.domain.store.service.command;

import com.study.demo.backend.domain.store.converter.StoreConverter;
import com.study.demo.backend.domain.store.dto.request.StoreReqDTO;
import com.study.demo.backend.domain.store.dto.response.StoreResDTO;
import com.study.demo.backend.domain.store.entity.Store;
import com.study.demo.backend.domain.store.exception.StoreErrorCode;
import com.study.demo.backend.domain.store.exception.StoreException;
import com.study.demo.backend.domain.store.repository.StoreRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {

    private final StoreRepository storeRepository;

    @Override
    public StoreResDTO.Create createStore(StoreReqDTO.@Valid Create create) {


        if (storeRepository.existsByName(create.name())) {
            throw new StoreException(StoreErrorCode.STORE_ALREADY_EXISTS);
        }

        Store store = StoreConverter.toEntity(create);
        storeRepository.save(store);

        return StoreConverter.toCreateDTO(store);
    }

    @Override
    public StoreResDTO.Update updateStore(Long storeId, StoreReqDTO.@Valid Update updateDTO) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        store.update(
                updateDTO.name(),
                updateDTO.latitude(),
                updateDTO.longitude(),
                updateDTO.openingTime(),
                updateDTO.closingTime()
        );

        return StoreConverter.toUpdateDTO(store);
    }
}
