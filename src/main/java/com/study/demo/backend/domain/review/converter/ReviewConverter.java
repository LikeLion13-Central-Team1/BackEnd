package com.study.demo.backend.domain.review.converter;

import com.study.demo.backend.domain.menu.entity.Menu;
import com.study.demo.backend.domain.order.entity.Order;
import com.study.demo.backend.domain.order.entity.OrderMenu;
import com.study.demo.backend.domain.review.dto.request.ReviewReqDTO;
import com.study.demo.backend.domain.review.dto.response.ReviewResDTO;
import com.study.demo.backend.domain.review.entity.Review;
import com.study.demo.backend.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewConverter {

    public static Review toReview(User user, Order order, ReviewReqDTO.WriteReview reqDTO) {
        return Review.builder()
                .user(user)
                .order(order)
                .store(order.getStore())
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
        Order order = review.getOrder();

        return ReviewResDTO.ReviewDetail.builder()
                .reviewId(review.getId())
                .reviewContent(review.getContent())
                .reviewDate(review.getReviewDate())
                .orders(toOrderInfo(order))
                .build();
    }

    private static ReviewResDTO.OrderInfo toOrderInfo(Order order) {
        List<ReviewResDTO.MenuInfo> menuInfos = order.getOrderMenus().stream()
                .map(ReviewConverter::toMenuInfo)
                .toList();

        return ReviewResDTO.OrderInfo.builder()
                .orderId(order.getId())
                .menus(menuInfos)
                .build();
    }

    private static ReviewResDTO.MenuInfo toMenuInfo(OrderMenu orderMenu) {
        return ReviewResDTO.MenuInfo.builder()
                .name(orderMenu.getMenu().getName())
                .quantity(orderMenu.getQuantity())
                .build();
    }
}
