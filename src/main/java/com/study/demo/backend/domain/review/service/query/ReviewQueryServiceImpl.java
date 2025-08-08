package com.study.demo.backend.domain.review.service.query;

import com.study.demo.backend.domain.review.converter.ReviewConverter;
import com.study.demo.backend.domain.review.dto.request.ReviewReqDTO;
import com.study.demo.backend.domain.review.dto.response.ReviewResDTO;
import com.study.demo.backend.domain.review.entity.Review;
import com.study.demo.backend.domain.review.entity.enums.TargetType;
import com.study.demo.backend.domain.review.repository.ReviewRepository;
import com.study.demo.backend.domain.review.service.command.GPTService;
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
    private final GPTService gptService;

    @Override
    public ReviewResDTO.ReviewDetailList getReviewsByTarget(
            TargetType targetType,
            Long targetId,
            Long cursor,
            int size) {

        LocalDateTime cursorDate = convertCursorToDate(cursor);
        Pageable pageable = PageRequest.of(0, size + 1);

        List<Review> reviews = fetchReviewsByTargetType(targetType, targetId, cursorDate, pageable);

        boolean hasNext = reviews.size() > size;
        if (hasNext) {
            reviews = reviews.subList(0, size);
        }

        Long nextCursor = hasNext && !reviews.isEmpty()
                ? convertDateToCursor(reviews.get(reviews.size() - 1).getReviewDate())
                : null;

        List<ReviewResDTO.ReviewDetail> reviewDetails = reviews.stream()
                .map(ReviewConverter::toReviewDetail)
                .toList();

        return ReviewResDTO.ReviewDetailList.builder()
                .reviews(reviewDetails)
                .hasNext(hasNext)
                .cursor(nextCursor)
                .build();
    }

    @Override
    public ReviewResDTO.Summary summarizeReviewsByStore(Long storeId) {
        List<Review> reviews = reviewRepository.findAllByStoreIdOrderByReviewDateAtDesc(storeId);
        List<String> contents = reviews.stream().map(Review::getContent).toList();
        String summary = gptService.summarizeFromContents(contents);
        return new ReviewResDTO.Summary(summary);
    }

    private List<Review> fetchReviewsByTargetType(
            TargetType targetType,
            Long targetId,
            LocalDateTime cursorDate,
            Pageable pageable) {

        return switch (targetType) {
            case MENU -> reviewRepository.findReviewsByMenuId(targetId, cursorDate, pageable);
            case STORE -> reviewRepository.findReviewsByStoreId(targetId, cursorDate, pageable);
        };
    }

    private LocalDateTime convertCursorToDate(Long cursor) {
        if (cursor == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(cursor), ZoneId.systemDefault());
    }

    private Long convertDateToCursor(LocalDateTime date) {
        return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
