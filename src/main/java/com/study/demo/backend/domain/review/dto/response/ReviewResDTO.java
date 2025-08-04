package com.study.demo.backend.domain.review.dto.response;

import lombok.Builder;

import java.time.LocalDate;

public class ReviewResDTO {

    @Builder
    public record WriteReview(
            String content,
            LocalDate reviewDate
    ){
    }
}
