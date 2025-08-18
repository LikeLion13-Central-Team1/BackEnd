package com.study.demo.backend.domain.favorite.exception;

import com.study.demo.backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class FavoriteException extends CustomException {

    public FavoriteException(FavoriteErrorCode errorCode){
        super(errorCode);
    }
}