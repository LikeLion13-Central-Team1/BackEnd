package com.study.demo.backend.domain.store.exception;

import com.study.demo.backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class StoreException extends CustomException {

    public StoreException(StoreErrorCode errorCode){
        super(errorCode);
    }
}