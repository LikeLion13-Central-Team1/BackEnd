package com.study.demo.backend.domain.store.converter;

import com.study.demo.backend.domain.store.dto.request.StoreReqDTO;
import com.study.demo.backend.domain.store.dto.response.StoreResDTO;
import com.study.demo.backend.domain.store.entity.Store;
import com.study.demo.backend.domain.user.entity.User;

import java.time.LocalTime;

public class StoreConverter {

    public static Store toEntity(StoreReqDTO.CreateStoreReq dto, String imageUrl, User owner) {
        return Store.builder()
                .user(owner)
                .name(dto.name())
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .openingTime(dto.openingTime())
                .closingTime(dto.closingTime())
                .imageUrl(imageUrl)
                .build();
    }

    public static StoreResDTO.CreateStoreRes toCreateDTO(Store store) {
        return new StoreResDTO.CreateStoreRes(
                store.getId(),
                store.getName(),
                store.getOpeningTime(),
                store.getClosingTime(),
                store.getLatitude(),
                store.getLongitude(),
                store.getCreatedAt()
        );
    }

    public static StoreResDTO.StoreDetail toStoreDetailDTO(Store s) {
        return toStoreDetailDTO(s, false);
    }

    public static StoreResDTO.StoreDetail toStoreDetailDTO(Store store, boolean favorited) {
        return StoreResDTO.StoreDetail.builder()
                .storeId(store.getId())
                .name(store.getName())
                .roadAddressName(store.getRoadAddressName())
                .imageUrl(store.getImageUrl())
                .openingTime(store.getOpeningTime())
                .closingTime(store.getClosingTime())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .createdAt(store.getCreatedAt())
                .favorited(favorited)
                .build();
    }

    public static StoreResDTO.UpdateStoreRes toUpdateDTO(Store store) {
        return StoreResDTO.UpdateStoreRes.builder()
                .storeId(store.getId())
                .name(store.getName())
                .openingTime(store.getOpeningTime())
                .closingTime(store.getClosingTime())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .updatedAt(store.getUpdatedAt())
                .imageUrl(store.getImageUrl())
                .build();
    }

    public static StoreResDTO.CloseRes toCloseRes(Store store,LocalTime previousClosingTime,LocalTime newClosingTime) {
        return StoreResDTO.CloseRes.builder()
                .storeId(store.getId())
                .previousClosingTime(previousClosingTime)
                .newClosingTime(newClosingTime)
                .build();
    }

    public static StoreResDTO.OpenRes toOpenRes(Store store,LocalTime previousOpeningTime,LocalTime newOpeningTime) {
        return StoreResDTO.OpenRes.builder()
                .storeId(store.getId())
                .previousOpeningTime(previousOpeningTime)
                .newOpeningTime(newOpeningTime)
                .build();
    }
}
