package com.study.demo.backend.domain.user.service.query;

import com.study.demo.backend.domain.user.dto.response.UserLocationResDTO;
import com.study.demo.backend.global.security.userdetails.AuthUser;

public interface UserLocationQueryService {

    UserLocationResDTO.LocationInfoList getLocationList(AuthUser authUser);
    UserLocationResDTO.LocationInfo getActiveLocation(AuthUser authUser);
}
