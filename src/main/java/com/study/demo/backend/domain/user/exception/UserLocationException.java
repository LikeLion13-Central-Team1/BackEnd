package com.study.demo.backend.domain.user.exception;

import com.study.demo.backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class UserLocationException extends CustomException {

    public UserLocationException(UserLocationErrorCode errorCode){
        super(errorCode);
    }
}
