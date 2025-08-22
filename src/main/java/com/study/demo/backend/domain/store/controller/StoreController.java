package com.study.demo.backend.domain.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.demo.backend.domain.store.dto.request.StoreReqDTO;
import com.study.demo.backend.domain.store.dto.response.StoreResDTO;
import com.study.demo.backend.domain.store.entity.Store;
import com.study.demo.backend.domain.store.entity.storeEnums.StoreSortType;
import com.study.demo.backend.domain.store.exception.StoreErrorCode;
import com.study.demo.backend.domain.store.exception.StoreException;
import com.study.demo.backend.domain.store.repository.StoreRepository;
import com.study.demo.backend.domain.store.service.command.StoreCommandService;
import com.study.demo.backend.domain.store.service.query.StoreQueryService;
import com.study.demo.backend.domain.user.entity.enums.Role;
import com.study.demo.backend.global.apiPayload.CustomResponse;
import com.study.demo.backend.global.apiPayload.exception.CustomException;
import com.study.demo.backend.global.security.annotation.CurrentUser;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/store")
@Tag(name = "가게 API", description = "가게 관련 API")
@RequiredArgsConstructor
public class StoreController {

    private final StoreRepository storeRepository;
    private final StoreQueryService storeQueryService;
    private final StoreCommandService storeCommandService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "가게 등록 API by 최현우", description = "사용자의 Role이 OWNER인 경우 가게를 등록합니다.")
    public CustomResponse<StoreResDTO.CreateStoreRes> createStore(
            @RequestPart("create") String createJson,
            @RequestPart(value = "storeImage", required = false) MultipartFile storeImage,
            @CurrentUser AuthUser authUser
    ) throws JsonProcessingException {

        if (!authUser.getRole().equals(Role.OWNER)) {
            throw new CustomException(StoreErrorCode.STORE_ACCESS_DENIED);
        }

        StoreReqDTO.CreateStoreReq createDTO = objectMapper.readValue(createJson, StoreReqDTO.CreateStoreReq.class);

        StoreResDTO.CreateStoreRes storeResDTO = storeCommandService.createStore(createDTO, storeImage, authUser);

        return CustomResponse.onSuccess(storeResDTO);
    }

    @GetMapping("")
    @Operation(summary = "가게 목록 조회 API by 최현우", description = """
        가게 목록을 커서 기반 페이지네이션으로 조회합니다.  
        - 정렬 기준 : 'distance(거리순)', 'review(리뷰 많은 순)'
        - cursor : 마지막으로 조회한 storeId (기본 null)
        - size : 조회할 데이터 수 (기본 10개)
        - 'lat', 'lng' : 현재 위치 좌표 (기본 상명대 위치) """)
    public CustomResponse<StoreResDTO.StoreDetailList> getStoreList(
            @Parameter(description = "마지막으로 조회한 storeId")
            @RequestParam(required = false) Long cursor,

            @Parameter(description = "한 번에 조회할 가게 수", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "정렬 기준", example = "DISTANCE")
            @RequestParam(defaultValue = "DISTANCE") StoreSortType type,

            @Parameter(description = "현재 위도", example = "37.602")
            @RequestParam(required = false, defaultValue = "37.602") double lat,

            @Parameter(description = "현재 경도", example = "126.9565")
            @RequestParam(required = false, defaultValue = "126.9565") double lng,

            @CurrentUser AuthUser authUser
    ) {
        StoreResDTO.StoreDetailList storeDetailList = storeQueryService.getStoreList(cursor, size, type, lat, lng, authUser.getUserId());
        return CustomResponse.onSuccess(storeDetailList);
    }

    @GetMapping("/{storeId}")
    @Operation(summary = "가게 상세 조회 API by 최현우", description = "가게 상세 조회")
    public CustomResponse<StoreResDTO.StoreDetail> getStoreDetail(
            @PathVariable Long storeId,
            @CurrentUser AuthUser authUser
    ) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        StoreResDTO.StoreDetail storeDetail = storeQueryService.getStoreDetail(storeId, authUser.getUserId());
        return CustomResponse.onSuccess(storeDetail);
    }

    @PatchMapping(value = "/{storeId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "가게 정보 수정 API by 최현우", description = "사용자의 Role이 OWNER인 경우 가게 정보를 수정합니다.")
    public CustomResponse<StoreResDTO.UpdateStoreRes> updateStore(
            @PathVariable Long storeId,
            @RequestPart("update") String updateJson,
            @RequestPart(value = "storeImage", required = false) MultipartFile storeImage,
            @CurrentUser AuthUser authUser
    ) throws JsonProcessingException {
        if (!authUser.getRole().equals(Role.OWNER)) {
            throw new CustomException(StoreErrorCode.STORE_ACCESS_DENIED);
        }

        StoreReqDTO.UpdateStoreReq updateDTO = objectMapper.readValue(updateJson, StoreReqDTO.UpdateStoreReq.class);

        StoreResDTO.UpdateStoreRes response = storeCommandService.updateStore(storeId, updateDTO, storeImage, authUser);

        return CustomResponse.onSuccess(response);
    }
}

