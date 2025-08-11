package com.study.demo.backend.domain.user.converter;

import com.study.demo.backend.domain.user.dto.request.UserLocationReqDTO;
import com.study.demo.backend.domain.user.dto.response.UserLocationResDTO;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.domain.user.entity.UserLocation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLocationConverter {

    public static UserLocation toUserLocation(UserLocationReqDTO.Create reqDTO, User user) {
        return UserLocation.builder()
                .user(user)
                .latitude(reqDTO.latitude())
                .longitude(reqDTO.longitude())
                .name(reqDTO.name())
                .build();
    }

    public static UserLocationResDTO.LocationInfo toLocationInfo(UserLocation userLocation) {
        return UserLocationResDTO.LocationInfo.builder()
                .locationId(userLocation.getId())
                .locationName(userLocation.getName())
                .latitude(userLocation.getLatitude())
                .longitude(userLocation.getLongitude())
                .build();
    }
}
