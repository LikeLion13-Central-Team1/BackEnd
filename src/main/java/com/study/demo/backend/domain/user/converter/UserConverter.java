package com.study.demo.backend.domain.user.converter;

import com.study.demo.backend.domain.user.dto.response.UserResDTO;
import com.study.demo.backend.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserConverter {

    public static UserResDTO.UserInfo toUserInfo(User user) {
        return UserResDTO.UserInfo.builder()
                .nickname(user.getNickname())
                .role(user.getRole().name())
                .build();
    }
}
