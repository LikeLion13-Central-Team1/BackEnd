package com.study.demo.backend.domain.review.service.query;

import com.study.demo.backend.domain.review.dto.response.ReviewResDTO;
import com.study.demo.backend.domain.review.entity.enums.TargetType;

public interface ReviewQueryService {
    ReviewResDTO.ReviewDetailList getReviewsByTarget(TargetType type, Long targetId, Long cursor, int size);
}
