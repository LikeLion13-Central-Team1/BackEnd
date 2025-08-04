package com.study.demo.backend.domain.user.exception;

import com.study.demo.backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class UserException extends CustomException {

    public UserException(UserErrorCode errorCode){
        super(errorCode);
    }
}