package com.study.demo.backend.domain.menu.exception;

import com.study.demo.backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class MenuException extends CustomException {

    public MenuException(MenuErrorCode errorCode){
        super(errorCode);
    }
}