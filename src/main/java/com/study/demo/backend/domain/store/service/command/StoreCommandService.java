package com.study.demo.backend.domain.store.service.command;

import com.study.demo.backend.domain.store.dto.request.StoreReqDTO;
import com.study.demo.backend.domain.store.dto.response.StoreResDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface StoreCommandService {
    StoreResDTO.CreateStoreRes createStore(StoreReqDTO.@Valid CreateStoreReq createDTO);

    StoreResDTO.UpdateStoreRes updateStore(Long storeId, StoreReqDTO.@Valid UpdateStoreReq updateStoreReqDTO);
}
