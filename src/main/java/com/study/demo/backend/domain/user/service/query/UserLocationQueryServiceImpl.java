package com.study.demo.backend.domain.user.service.query;

import com.study.demo.backend.domain.user.converter.UserLocationConverter;
import com.study.demo.backend.domain.user.dto.response.UserLocationResDTO;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.domain.user.entity.UserLocation;
import com.study.demo.backend.domain.user.exception.UserErrorCode;
import com.study.demo.backend.domain.user.exception.UserException;
import com.study.demo.backend.domain.user.repository.UserLocationRepository;
import com.study.demo.backend.domain.user.repository.UserRepository;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLocationQueryServiceImpl implements UserLocationQueryService{

    private final UserRepository userRepository;
    private final UserLocationRepository userLocationRepository;

    @Override
    public UserLocationResDTO.LocationInfoList getLocationList(AuthUser authUser) {
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        List<UserLocation> userLocations = userLocationRepository.findAllLocationsByUserId(user.getId());

        List<UserLocationResDTO.LocationInfo> locationInfoList = userLocations.stream()
                .map(UserLocationConverter::toLocationInfo)
                .toList();

        return new UserLocationResDTO.LocationInfoList(locationInfoList);
    }
}
