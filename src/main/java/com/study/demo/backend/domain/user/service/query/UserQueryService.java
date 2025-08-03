package com.study.demo.backend.domain.user.service.query;

import com.study.demo.backend.domain.user.dto.response.UserResDTO;

public interface UserQueryService {
    UserResDTO.UserInfo getUserInfo(String email);
}
