package com.study.demo.backend.domain.review.service.command;

import com.study.demo.backend.domain.order.entity.Order;
import com.study.demo.backend.domain.order.exception.OrderErrorCode;
import com.study.demo.backend.domain.order.exception.OrderException;
import com.study.demo.backend.domain.order.repository.OrderRepository;
import com.study.demo.backend.domain.review.converter.ReviewConverter;
import com.study.demo.backend.domain.review.dto.request.ReviewReqDTO;
import com.study.demo.backend.domain.review.dto.response.ReviewResDTO;
import com.study.demo.backend.domain.review.entity.Review;
import com.study.demo.backend.domain.review.repository.ReviewRepository;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.domain.user.repository.UserRepository;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewCommandServiceImpl implements ReviewCommandService{

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    // Todo : PR 머지 후 RuntimeException => UserException으로 변경하기
    @Override
    public ReviewResDTO.WriteReview writeReview(AuthUser authUser, ReviewReqDTO.WriteReview reqDTO) {
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new RuntimeException("PR 머지 후 수정 예정"));

        Order order = orderRepository.findById(reqDTO.orderId())
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

        Review review = ReviewConverter.toReview(user, order, reqDTO);
        reviewRepository.save(review);

        return ReviewConverter.toWriteReviewResponse(review);
    }
}
