package com.study.demo.backend.domain.review.converter;

import com.study.demo.backend.domain.order.entity.Order;
import com.study.demo.backend.domain.review.dto.request.ReviewReqDTO;
import com.study.demo.backend.domain.review.dto.response.ReviewResDTO;
import com.study.demo.backend.domain.review.entity.Review;
import com.study.demo.backend.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewConverter {

    public static Review toReview(User user, Order order, ReviewReqDTO.WriteReview reqDTO) {
        return Review.builder()
                .user(user)
                .order(order)
                .content(reqDTO.content())
                .reviewDate(LocalDateTime.now())
                .build();
    }

    public static ReviewResDTO.WriteReview toWriteReviewResponse(Review review) {
        return ReviewResDTO.WriteReview.builder()
                .content(review.getContent())
                .reviewDate(review.getReviewDate())
                .build();
    }

    public static ReviewResDTO.ReviewDetail toReviewDetail(Review review) {
        return ReviewResDTO.ReviewDetail.builder()
                .reviewId(review.getId())
                .reviewContent(review.getContent())
                .reviewDate(review.getReviewDate())
                .orders(ReviewResDTO.OrderInfo.builder()
                        .orderId(review.getOrder().getId())
                        .menus(
                                review.getOrder().getOrderMenus().stream()
                                        .map(om -> ReviewResDTO.MenuInfo.builder()
                                                .name(om.getMenu().getName())
                                                .quantity(om.getQuantity())
                                                .build())
                                        .toList()
                        )
                        .build()
                )
                .build();
    }
}
