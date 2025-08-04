package com.study.demo.backend.domain.review.service.query;

import com.study.demo.backend.domain.review.converter.ReviewConverter;
import com.study.demo.backend.domain.review.dto.response.ReviewResDTO;
import com.study.demo.backend.domain.review.entity.Review;
import com.study.demo.backend.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewQueryServiceImpl implements ReviewQueryService {

    private final ReviewRepository reviewRepository;

    @Override
    public ReviewResDTO.ReviewDetailList getReviewsByMenu(Long menuId, Long cursor, int offset) {
        LocalDateTime cursorDateTime = (cursor != null)
                ? LocalDateTime.ofInstant(Instant.ofEpochMilli(cursor), ZoneId.systemDefault())
                : LocalDateTime.now();

        Pageable pageable = PageRequest.of(0, offset + 1);

        List<Review> reviews = reviewRepository.findReviewsByMenuIdBefore(menuId, cursorDateTime, pageable);

        boolean hasNext = reviews.size() > offset;
        if (hasNext) reviews.remove(reviews.size() - 1);

        Long nextCursor = reviews.isEmpty() ? null :
                reviews.get(reviews.size() - 1).getReviewDate()
                        .atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli();

        List<ReviewResDTO.ReviewDetail> reviewDTOs = reviews.stream()
                .map(ReviewConverter::toReviewDetail)
                .toList();

        return ReviewResDTO.ReviewDetailList.builder()
                .reviews(reviewDTOs)
                .hasNext(hasNext)
                .cursor(nextCursor)
                .build();
    }
}
