package com.study.demo.backend.domain.review.service.query;

import com.study.demo.backend.domain.review.dto.response.ReviewResDTO;

public interface ReviewQueryService {
    ReviewResDTO.ReviewDetailList getReviewsByMenu(Long menuId, Long cursor, int offset);
}
