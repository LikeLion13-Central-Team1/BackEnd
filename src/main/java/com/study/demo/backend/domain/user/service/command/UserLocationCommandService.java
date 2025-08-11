package com.study.demo.backend.domain.user.service.command;

import com.study.demo.backend.domain.user.dto.request.UserLocationReqDTO;
import com.study.demo.backend.domain.user.dto.response.UserLocationResDTO;
import com.study.demo.backend.global.security.userdetails.AuthUser;

public interface UserLocationCommandService {

    UserLocationResDTO.LocationInfo createLocation(UserLocationReqDTO.Create reqDTO, AuthUser authUser);
}
