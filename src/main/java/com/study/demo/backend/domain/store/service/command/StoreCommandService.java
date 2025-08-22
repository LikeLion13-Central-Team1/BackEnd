package com.study.demo.backend.domain.store.service.command;

import com.study.demo.backend.domain.store.dto.request.StoreReqDTO;
import com.study.demo.backend.domain.store.dto.response.StoreResDTO;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface StoreCommandService {
    StoreResDTO.CreateStoreRes createStore(StoreReqDTO.@Valid CreateStoreReq createDTO, MultipartFile storeImage, AuthUser authUser);

    StoreResDTO.UpdateStoreRes updateStore(Long storeId, StoreReqDTO.@Valid UpdateStoreReq updateStoreReqDTO, MultipartFile storeImage, AuthUser authUser);
}
