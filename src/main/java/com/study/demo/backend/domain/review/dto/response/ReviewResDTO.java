package com.study.demo.backend.domain.review.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResDTO {

    @Builder
    public record WriteReview(
            String content,
            LocalDateTime reviewDate
    ){
    }

    @Builder
    public record ReviewDetail(
            Long reviewId,
            String reviewContent,
            LocalDateTime reviewDate,
            OrderInfo orders
    ) {
    }

    @Builder
    public record OrderInfo(
            Long orderId,
            List<MenuInfo> menus
    ) {
    }

    @Builder
    public record MenuInfo(
            String name,
            int quantity
    ) {
    }

    @Builder
    public record ReviewDetailList(
            List<ReviewDetail> reviews,
            boolean hasNext,
            Long cursor
    ) {
    }

}
