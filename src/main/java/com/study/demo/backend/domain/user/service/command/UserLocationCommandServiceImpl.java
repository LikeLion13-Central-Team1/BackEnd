package com.study.demo.backend.domain.user.service.command;

import com.study.demo.backend.domain.user.converter.UserLocationConverter;
import com.study.demo.backend.domain.user.dto.request.UserLocationReqDTO;
import com.study.demo.backend.domain.user.dto.response.UserLocationResDTO;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.domain.user.entity.UserLocation;
import com.study.demo.backend.domain.user.exception.UserErrorCode;
import com.study.demo.backend.domain.user.exception.UserException;
import com.study.demo.backend.domain.user.exception.UserLocationErrorCode;
import com.study.demo.backend.domain.user.exception.UserLocationException;
import com.study.demo.backend.domain.user.repository.UserLocationRepository;
import com.study.demo.backend.domain.user.repository.UserRepository;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLocationCommandServiceImpl implements UserLocationCommandService{

    private final UserRepository userRepository;
    private final UserLocationRepository userLocationRepository;

    @Override
    public UserLocationResDTO.LocationInfo createLocation(UserLocationReqDTO.Create reqDTO, AuthUser authUser) {
        User user = findUser(authUser);

        UserLocation userLocation = UserLocationConverter.toUserLocation(reqDTO, user);
        userLocationRepository.save(userLocation);

        return UserLocationConverter.toLocationInfo(userLocation);
    }

    @Override
    @Transactional
    public void changeActiveLocation(Long locationId, AuthUser authUser) {
        User user = findUser(authUser);

        UserLocation targetLocation = userLocationRepository.findById(locationId)
                .orElseThrow(() -> new UserLocationException(UserLocationErrorCode.USER_LOCATION_NOT_FOUND));

        if(!targetLocation.getUser().getId().equals(user.getId())) {
            throw new UserLocationException(UserLocationErrorCode.LOCATION_ACCESS_DENIED);
        }

        userLocationRepository.findActiveLocationByUserId(user.getId())
                .ifPresent(activeLocation -> activeLocation.updateActive(false));

        targetLocation.updateActive(true);
    }

    private User findUser(AuthUser authUser) {
        return userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }
}
