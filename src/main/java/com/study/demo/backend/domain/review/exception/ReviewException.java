package com.study.demo.backend.domain.review.exception;

import com.study.demo.backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class ReviewException extends CustomException {

    public ReviewException(ReviewErrorCode errorCode){
        super(errorCode);
    }
}
