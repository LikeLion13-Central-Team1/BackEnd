package com.study.demo.backend.domain.order.exception;

import com.study.demo.backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class OrderException extends CustomException {

    public OrderException(OrderErrorCode errorCode){
        super(errorCode);
    }
}
