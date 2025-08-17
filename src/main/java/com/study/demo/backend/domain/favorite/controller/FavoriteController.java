package com.study.demo.backend.domain.favorite.controller;

import com.study.demo.backend.domain.favorite.dto.response.FavoriteResDTO;
import com.study.demo.backend.domain.favorite.exception.FavoriteErrorCode;
import com.study.demo.backend.domain.favorite.service.FavoriteMenuService.command.FavoriteMenuCommandService;
import com.study.demo.backend.domain.favorite.service.FavoriteMenuService.query.FavoriteMenuQueryService;
import com.study.demo.backend.domain.favorite.service.FavoriteStoreService.command.FavoriteStoreCommandService;
import com.study.demo.backend.domain.favorite.service.FavoriteStoreService.query.FavoriteStoreQueryService;
import com.study.demo.backend.domain.user.entity.enums.Role;
import com.study.demo.backend.global.apiPayload.CustomResponse;
import com.study.demo.backend.global.apiPayload.exception.CustomException;
import com.study.demo.backend.global.security.annotation.CurrentUser;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1")
@Tag(name = "찜 API", description = "가게 찜, 메뉴 찜 관련 API")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteMenuCommandService favoriteMenuCommandService;
    private final FavoriteStoreCommandService favoriteStoreCommandService;
    private final FavoriteMenuQueryService favoriteMenuQueryService;
    private final FavoriteStoreQueryService favoriteStoreQueryService;

    @PostMapping("/menus/{menuId}/favorite")
    @Operation(summary = "메뉴 찜 추가", description = "해당 메뉴를 사용자의 찜에 추가합니다.")
    public CustomResponse<FavoriteResDTO.Status> createMenuFavorite(
            @CurrentUser AuthUser authUser,
            @PathVariable Long menuId
    ) {
        if (!authUser.getRole().equals(Role.CUSTOMER)) {
            throw new CustomException(FavoriteErrorCode.FAVORITE_ACCESS_DENIED);
        }

        favoriteMenuCommandService.create(authUser.getUserId(), menuId);
        FavoriteResDTO.Status status = new FavoriteResDTO.Status(true);
        return CustomResponse.onSuccess(status);
    }

    @GetMapping("/favorites/menus")
    @Operation(
        summary = "내 메뉴 찜 목록 조회 (최신순, 무한 스크롤)",
        description = """
            커서 기반 페이지네이션입니다.
            - 정렬: 찜 최신순 (favoriteId DESC)
            - cursor: 마지막으로 받은 favoriteId (없으면 null)
            - size: 한 번에 가져올 개수 (기본 10, 1~50)
            """
    )
    public CustomResponse<FavoriteResDTO.FavoriteMenuList> getMenuFavorites(
            @CurrentUser AuthUser authUser,
            @Parameter(description = "마지막으로 조회한 favoriteId", example = "125")
            @RequestParam(required = false) Long cursor,
            @Parameter(description = "가져올 개수 (1~50)", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size
    ) {
        if (!authUser.getRole().equals(Role.CUSTOMER)) {
            throw new CustomException(FavoriteErrorCode.FAVORITE_ACCESS_DENIED);
        }

        FavoriteResDTO.FavoriteMenuList favoriteMenuList = favoriteMenuQueryService.findMyFavorites(authUser.getUserId(), cursor, size);
        return CustomResponse.onSuccess(favoriteMenuList);
    }

    @DeleteMapping("/favorites/menus/{menuId}")
    @Operation(summary = "메뉴 찜 삭제", description = "해당 메뉴를 사용자의 찜에서 삭제합니다.")
    public CustomResponse<FavoriteResDTO.Status> deleteMenuFavorite(
            @CurrentUser AuthUser authUser,
            @PathVariable Long menuId
    ) {
        if (!authUser.getRole().equals(Role.CUSTOMER)) {
            throw new CustomException(FavoriteErrorCode.FAVORITE_ACCESS_DENIED);
        }

        favoriteMenuCommandService.delete(authUser.getUserId(), menuId);
        return CustomResponse.onSuccess(new FavoriteResDTO.Status(false));
    }


/*
    // ========================= 가게 =========================
    */

    @PostMapping("/favorites/stores/{storeId}")
    @Operation(summary = "가게 찜 추가", description = "해당 가게를 사용자의 찜에 추가합니다.")
    public CustomResponse<FavoriteResDTO.Status> addStoreFavorite(
            @CurrentUser AuthUser authUser,
            @PathVariable Long storeId
    ) {
        if (!authUser.getRole().equals(Role.CUSTOMER)) {
            throw new CustomException(FavoriteErrorCode.FAVORITE_ACCESS_DENIED);
        }
        favoriteStoreCommandService.create(authUser.getUserId(), storeId);
        FavoriteResDTO.Status status = new FavoriteResDTO.Status(true);
        return CustomResponse.onSuccess(status);
    }

    @GetMapping("/favorites/stores")
    @Operation(
        summary = "내 가게 찜 목록 조회 (최신순, 무한 스크롤)",
        description = """
            커서 기반 페이지네이션입니다.
            - 정렬: 찜 최신순 (favoriteId DESC)
            - cursor: 마지막으로 받은 favoriteId (없으면 null)
            - size: 한 번에 가져올 개수 (기본 10, 1~50)
            """
    )
    public CustomResponse<FavoriteResDTO.FavoriteStoreList> getStoreFavorites(
            @CurrentUser AuthUser authUser,
            @Parameter(description = "마지막으로 조회한 favoriteId", example = "88")
            @RequestParam(required = false) Long cursor,
            @Parameter(description = "가져올 개수 (1~50)", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size
    ) {
        if (!authUser.getRole().equals(Role.CUSTOMER)) {
            throw new CustomException(FavoriteErrorCode.FAVORITE_ACCESS_DENIED);
        }
        FavoriteResDTO.FavoriteStoreList favoriteStoreList = favoriteStoreQueryService.findMyFavorites(authUser.getUserId(), cursor, size);
        return CustomResponse.onSuccess(favoriteStoreList);
    }

    @DeleteMapping("/favorites/stores/{storeId}")
    @Operation(summary = "가게 찜 삭제", description = "해당 가게를 사용자의 찜에서 삭제합니다.")
    public CustomResponse<FavoriteResDTO.Status> removeStoreFavorite(
            @CurrentUser AuthUser authUser,
            @PathVariable Long storeId
    ) {
        if (!authUser.getRole().equals(Role.CUSTOMER)) {
            throw new CustomException(FavoriteErrorCode.FAVORITE_ACCESS_DENIED);
        }

        favoriteStoreCommandService.delete(authUser.getUserId(), storeId);
        return CustomResponse.onSuccess(new FavoriteResDTO.Status(false));
    }
}