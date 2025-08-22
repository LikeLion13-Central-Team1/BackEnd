package com.study.demo.backend.domain.store.service.command;

import com.study.demo.backend.domain.store.converter.StoreConverter;
import com.study.demo.backend.domain.store.dto.request.StoreReqDTO;
import com.study.demo.backend.domain.store.dto.response.StoreResDTO;
import com.study.demo.backend.domain.store.entity.Store;
import com.study.demo.backend.domain.store.exception.StoreErrorCode;
import com.study.demo.backend.domain.store.exception.StoreException;
import com.study.demo.backend.domain.store.repository.StoreRepository;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.domain.user.exception.UserErrorCode;
import com.study.demo.backend.domain.user.repository.UserRepository;
import com.study.demo.backend.global.ImgUploader.FileUploader;
import com.study.demo.backend.global.apiPayload.exception.CustomException;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StoreCommandServiceImpl implements StoreCommandService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final FileUploader fileUploader;

    @Override
    public StoreResDTO.CreateStoreRes createStore(StoreReqDTO.@Valid CreateStoreReq create, MultipartFile storeImage, AuthUser authUser) {
        if (storeRepository.existsByUser_Id(authUser.getUserId())) {
            throw new StoreException(StoreErrorCode.STORE_ALREADY_EXISTS_FOR_OWNER);
        }

        if (storeRepository.existsByName(create.name())) {
            throw new StoreException(StoreErrorCode.STORE_ALREADY_EXISTS);
        }

        if (create.latitude().compareTo(new BigDecimal("-90")) < 0 || create.latitude().compareTo(new BigDecimal("90")) > 0) {
            throw new StoreException(StoreErrorCode.INVALID_LOCATION);
        }

        if (create.longitude().compareTo(new BigDecimal("-180")) < 0 || create.longitude().compareTo(new BigDecimal("180")) > 0) {
            throw new StoreException(StoreErrorCode.INVALID_LOCATION);
        }

        if (create.openingTime() == null || create.closingTime() == null) {
            throw new StoreException(StoreErrorCode.INVALID_OPENING_CLOSING_TIME);
        }
        if (!create.openingTime().isBefore(create.closingTime())) {
            throw new StoreException(StoreErrorCode.INVALID_OPENING_CLOSING_TIME);
        }

        User owner = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        String imageUrl = (storeImage != null && !storeImage.isEmpty())
                ? fileUploader.upload(storeImage, "store")
                : null;

        Store store = StoreConverter.toEntity(create, imageUrl, owner);

        storeRepository.save(store);

        return StoreConverter.toCreateDTO(store);
    }

    @Override
    @Transactional
    public StoreResDTO.UpdateStoreRes updateStore(Long storeId, StoreReqDTO.@Valid UpdateStoreReq updateStoreReqDTO, MultipartFile storeImage, AuthUser authUser) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        if (!store.getUser().getId().equals(authUser.getUserId())) {
            throw new StoreException(StoreErrorCode.NO_PERMISSION_TO_UPDATE_STORE);
        }

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

        String imageUrl = (storeImage != null && !storeImage.isEmpty())
                ? fileUploader.upload(storeImage, "store")
                : null;

        // 수정 중 null인 값이 있으면 기존의 값으로!
        String name = (updateStoreReqDTO.name() != null && !updateStoreReqDTO.name().isBlank()) ? updateStoreReqDTO.name().trim() : store.getName();
        BigDecimal lat = (updateStoreReqDTO.latitude() != null) ? updateStoreReqDTO.latitude() : store.getLatitude();
        BigDecimal lng = (updateStoreReqDTO.longitude() != null) ? updateStoreReqDTO.longitude() : store.getLongitude();
        LocalTime open = (updateStoreReqDTO.openingTime() != null) ? updateStoreReqDTO.openingTime() : store.getOpeningTime();
        LocalTime close = (updateStoreReqDTO.closingTime() != null) ? updateStoreReqDTO.closingTime() : store.getClosingTime();
        String img = (imageUrl != null) ? imageUrl : store.getImageUrl();

        store.update(name, lat, lng, open, close, img);

        return StoreConverter.toUpdateDTO(store);
    }
}
