package com.study.demo.backend.domain.review.service.query;

import com.study.demo.backend.domain.review.dto.request.ReviewReqDTO;
import com.study.demo.backend.domain.review.dto.response.ReviewResDTO;
import com.study.demo.backend.domain.review.entity.enums.TargetType;
import com.study.demo.backend.global.security.userdetails.AuthUser;

public interface ReviewQueryService {
    ReviewResDTO.ReviewDetailList getReviewsByTarget(TargetType type, Long targetId, Long cursor, int size);
    ReviewResDTO.Summary summarizeReviewsByStore(Long storeId);
    ReviewResDTO.ReviewDetailList getReviewsByUser(AuthUser authUser, Long cursor, int size);
}
