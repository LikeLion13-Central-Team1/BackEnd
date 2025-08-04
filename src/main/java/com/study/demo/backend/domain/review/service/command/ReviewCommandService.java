package com.study.demo.backend.domain.review.service.command;

import com.study.demo.backend.domain.review.dto.request.ReviewReqDTO;
import com.study.demo.backend.domain.review.dto.response.ReviewResDTO;
import com.study.demo.backend.global.security.userdetails.AuthUser;

public interface ReviewCommandService {

    ReviewResDTO.WriteReview writeReview(AuthUser authUser, ReviewReqDTO.WriteReview reqDTO);
}
