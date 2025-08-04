package com.study.demo.backend.domain.auth.exception;

import com.study.demo.backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class AuthException extends CustomException {

    public AuthException(AuthErrorCode errorCode){
        super(errorCode);
    }
}
