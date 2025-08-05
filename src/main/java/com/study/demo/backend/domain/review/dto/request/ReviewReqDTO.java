package com.study.demo.backend.domain.review.dto.request;

public class ReviewReqDTO {

    public record WriteReview(
            Long orderId,
            String content
    ) {
    }
}
