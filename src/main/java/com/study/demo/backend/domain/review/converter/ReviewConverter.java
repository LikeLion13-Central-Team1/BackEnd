package com.study.demo.backend.domain.review.converter;

import com.study.demo.backend.domain.order.entity.Order;
import com.study.demo.backend.domain.review.dto.request.ReviewReqDTO;
import com.study.demo.backend.domain.review.dto.response.ReviewResDTO;
import com.study.demo.backend.domain.review.entity.Review;
import com.study.demo.backend.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewConverter {

    public static Review toReview(User user, Order order, ReviewReqDTO.WriteReview reqDTO) {
        return Review.builder()
                .user(user)
                .order(order)
                .content(reqDTO.content())
                .reviewDate(LocalDate.now())
                .build();
    }

    public static ReviewResDTO.WriteReview toWriteReviewResponse(Review review) {
        return ReviewResDTO.WriteReview.builder()
                .content(review.getContent())
                .reviewDate(review.getReviewDate())
                .build();
    }
}
