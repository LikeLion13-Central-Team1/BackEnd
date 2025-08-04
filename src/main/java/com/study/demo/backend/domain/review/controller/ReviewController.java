package com.study.demo.backend.domain.review.controller;

import com.study.demo.backend.domain.review.dto.request.ReviewReqDTO;
import com.study.demo.backend.domain.review.dto.response.ReviewResDTO;
import com.study.demo.backend.domain.review.service.command.ReviewCommandService;
import com.study.demo.backend.domain.review.service.query.ReviewQueryService;
import com.study.demo.backend.global.apiPayload.CustomResponse;
import com.study.demo.backend.global.security.annotation.CurrentUser;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewCommandService reviewCommandService;
    private final ReviewQueryService reviewQueryService;

    @PostMapping("")
    @Operation(summary = "리뷰 작성 API by 김지명", description = "리뷰를 작성합니다.")
    public CustomResponse<ReviewResDTO.WriteReview> writeReview(@CurrentUser AuthUser authUser,
                                                                @RequestBody ReviewReqDTO.WriteReview reqDTO) {
        ReviewResDTO.WriteReview resDTO = reviewCommandService.writeReview(authUser, reqDTO);
        return CustomResponse.onSuccess(resDTO);
    }

    @GetMapping("")
    public CustomResponse<ReviewResDTO.ReviewDetailList> getReviewsByMenu(
            @RequestParam("menuId") Long menuId,
            @RequestParam(value = "cursor", required = false) Long cursor,
            @RequestParam(value = "offset", defaultValue = "10") int size) {

        ReviewResDTO.ReviewDetailList resDTO =
                reviewQueryService.getReviewsByMenu(menuId, cursor, size);

        return CustomResponse.onSuccess(resDTO);
    }
}
