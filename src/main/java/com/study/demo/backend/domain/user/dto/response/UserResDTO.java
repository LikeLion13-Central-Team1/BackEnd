package com.study.demo.backend.domain.user.dto.response;

import com.study.demo.backend.domain.user.entity.enums.Role;
import lombok.Builder;

public class UserResDTO {

    @Builder
    public record UserInfo(
            String nickname,
            String role
    ) {
    }
}
