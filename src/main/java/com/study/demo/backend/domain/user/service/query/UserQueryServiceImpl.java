package com.study.demo.backend.domain.user.service.query;

import com.study.demo.backend.domain.user.converter.UserConverter;
import com.study.demo.backend.domain.user.dto.response.UserResDTO;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.domain.user.exception.UserErrorCode;
import com.study.demo.backend.domain.user.exception.UserException;
import com.study.demo.backend.domain.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    @Override
    public UserResDTO.UserInfo getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        return UserConverter.toUserInfo(user);
    }
}
