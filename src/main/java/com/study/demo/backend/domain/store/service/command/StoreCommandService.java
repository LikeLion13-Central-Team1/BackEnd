package com.study.demo.backend.domain.store.service.command;

import com.study.demo.backend.domain.store.dto.request.StoreReqDTO;
import com.study.demo.backend.domain.store.dto.response.StoreResDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface StoreCommandService {
    StoreResDTO.Create createStore(StoreReqDTO.@Valid Create createDTO);

    StoreResDTO.Update updateStore(Long storeId, StoreReqDTO.@Valid Update updateDTO);
}
