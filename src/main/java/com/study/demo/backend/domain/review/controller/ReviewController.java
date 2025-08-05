package com.study.demo.backend.domain.review.controller;

import com.study.demo.backend.domain.review.dto.request.ReviewReqDTO;
import com.study.demo.backend.domain.review.dto.response.ReviewResDTO;
import com.study.demo.backend.domain.review.entity.enums.TargetType;
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
    @Operation(
            summary = "리뷰 목록 조회 API by 김지명",
            description = """
        메뉴 또는 가게(store) 기준으로 리뷰 목록을 커서 기반 페이지네이션 방식으로 조회합니다.
        - `targetType`에 따라 `MENU` 또는 `STORE` 값을 설정하고
        - `targetId`에 해당하는 메뉴 ID 또는 스토어 ID를 지정하세요.
        - `cursor`는 reviewDate의 에폭 밀리초 값입니다.
        """
    )
    public CustomResponse<ReviewResDTO.ReviewDetailList> getReviewsByMenu(
            @RequestParam("targetType") TargetType targetType,
            @RequestParam("targetId") Long targetId,
            @RequestParam(value = "cursor", required = false) Long cursor,
            @RequestParam(value = "offset", defaultValue = "10") int size) {

        ReviewResDTO.ReviewDetailList resDTO =
                reviewQueryService.getReviewsByTarget(targetType, targetId, cursor, size);

        return CustomResponse.onSuccess(resDTO);
    }
}
