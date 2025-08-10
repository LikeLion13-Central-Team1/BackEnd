package com.study.demo.backend.domain.cart.exception;

import com.study.demo.backend.domain.review.exception.ReviewErrorCode;
import com.study.demo.backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class CartException extends CustomException {

    public CartException(CartErrorCode errorCode){
        super(errorCode);
    }
}
