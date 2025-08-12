package com.study.demo.backend.domain.menu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.demo.backend.domain.menu.dto.request.MenuReqDTO;
import com.study.demo.backend.domain.menu.dto.response.MenuResDTO;
import com.study.demo.backend.domain.menu.entity.menuEnums.MenuSortType;
import com.study.demo.backend.domain.menu.exception.MenuErrorCode;
import com.study.demo.backend.domain.menu.repository.MenuRepository;
import com.study.demo.backend.domain.menu.service.command.MenuCommandService;
import com.study.demo.backend.domain.menu.service.query.MenuQueryService;
import com.study.demo.backend.domain.user.entity.enums.Role;
import com.study.demo.backend.global.apiPayload.CustomResponse;
import com.study.demo.backend.global.apiPayload.exception.CustomException;
import com.study.demo.backend.global.security.annotation.CurrentUser;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "메뉴 API", description = "메뉴 관련 API")
@RequiredArgsConstructor
public class MenuController {

    private final MenuRepository menuRepository;

    private final MenuQueryService menuQueryService;
    private final MenuCommandService menuCommandService;

    @PostMapping(value = "/store/{storeId}/menu", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "메뉴 등록 API by 최현우", description = "사용자의 Role이 OWNER인 경우 메뉴를 등록합니다.")
    public CustomResponse<MenuResDTO.CreateMenu> createMenu(
            @PathVariable Long storeId,
            @RequestPart("create") String createJson,
            @RequestPart(value = "menuImage", required = false) MultipartFile menuImage,
            @CurrentUser AuthUser authUser
    ) throws JsonProcessingException {

        if (!authUser.getRole().equals(Role.OWNER)) {
            throw new CustomException(MenuErrorCode.MENU_ACCESS_DENIED);
        }

        MenuReqDTO.CreateMenu create = new ObjectMapper().readValue(createJson, MenuReqDTO.CreateMenu.class);
        MenuResDTO.CreateMenu createMenu = menuCommandService.createMenu(storeId, create, menuImage);

        return CustomResponse.onSuccess(createMenu);
    }


    @GetMapping("/store/{storeId}/menus")
    @Operation(summary = "가게별 메뉴 목록 조회 API by 최현우", description = """
            가게 목록을 커서 기반 페이지네이션으로 조회합니다.
            - 정렬 기준 : '가격 오름차순', '가격 내림차순', '할인율 순'
            - cursor : 마지막으로 조회한 meuuId (기본 null)
            - size : 조회할 데이터 수 (기본 10개) """)
    public CustomResponse<MenuResDTO.MenuDetailList> getStoreMenuList(
            @Parameter(description = "가게 ID", example = "3")
            @PathVariable Long storeId,

            @Parameter(description = "마지막으로 조회한 storeId")
            @RequestParam(required = false) Long cursor,

            @Parameter(description = "한 번에 조회할 가게 수", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "정렬 기준", example = "DISCOUNT")
            @RequestParam(defaultValue = "DISCOUNT") MenuSortType menuSortType
    ) {
        MenuResDTO.MenuDetailList menuDetailList = menuQueryService.getStoreMenus(storeId, cursor, size, menuSortType);
        return CustomResponse.onSuccess(menuDetailList);
    }

    @GetMapping("/menus")
    @Operation(summary = "메뉴 목록 조회 API by 최현우", description = """
            가게 목록을 커서 기반 페이지네이션으로 조회합니다. 가게에 구애 받지 않고 메뉴들의 목록이 전부 뜨도록합니다.
            - 정렬 기준 : '가격 오름차순', '가격 내림차순', '할인율 순'
            - cursor : 마지막으로 조회한 meuuId (기본 null)
            - size : 조회할 데이터 수 (기본 10개) """)
    public CustomResponse<MenuResDTO.MenuDetailList> getMenuList(
            @Parameter(description = "마지막으로 조회한 storeId")
            @RequestParam(required = false) Long cursor,

            @Parameter(description = "한 번에 조회할 가게 수", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "정렬 기준", example = "DISCOUNT")
            @RequestParam(defaultValue = "DISCOUNT") MenuSortType menuSortType
    ) {
        MenuResDTO.MenuDetailList menuDetailList = menuQueryService.getMenus(cursor, size, menuSortType);
        return CustomResponse.onSuccess(menuDetailList);
    }

    @GetMapping("store/{storeId}/menu/{menuId}")
    @Operation(summary = "메뉴 상세 조회 API by 최현우", description = "특정 가게의 특정 메뉴 상세 정보를 조회합니다.")
    public CustomResponse<MenuResDTO.MenuDetail> getMenuDetail(
            @Parameter(description = "가게 ID", example = "1") @PathVariable Long storeId,
            @Parameter(description = "메뉴 ID", example = "3") @PathVariable Long menuId
    ) {
        MenuResDTO.MenuDetail menuDetail = menuQueryService.getMenuDetail(storeId, menuId);
        return CustomResponse.onSuccess(menuDetail);
    }

    @PatchMapping(value = "/store/{storeId}/menus/{menuId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "메뉴 내용 수정 API by 최현우",
            description = " role이 OWNER인 경우에만, 요청된 수정 사항에 대해서 수정합니다.")
    public CustomResponse<MenuResDTO.UpdateMenu> updateMenu(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestPart("update") String updateJson,
            @RequestPart(value = "menuImage", required = false) MultipartFile menuImage,
            @CurrentUser AuthUser authUser
    ) throws JsonProcessingException {

        if (!authUser.getRole().equals(Role.OWNER)) {
            throw new CustomException(MenuErrorCode.MENU_ACCESS_DENIED);
        }

        MenuReqDTO.UpdateMenu update =
                new ObjectMapper().readValue(updateJson, MenuReqDTO.UpdateMenu.class);

        MenuResDTO.UpdateMenu updateMenu =
                menuCommandService.updateMenu(storeId, menuId, update, menuImage);

        return CustomResponse.onSuccess(updateMenu);
    }

    @PatchMapping("/menus/{menuId}/quantity")
    @Operation(summary = "메뉴 재고 수정 API by 최현우", description = "role이 OWNER인 경우에 특정 메뉴의 할인율을 수정합니다.")
    public CustomResponse<MenuResDTO.UpdateMenu> changeQuantity(
            @PathVariable Long menuId,
            @RequestBody @Valid MenuReqDTO.ValueUpdate valueUpdate,
            @CurrentUser AuthUser authUser
    ) {
        if (!authUser.getRole().equals(Role.OWNER)) {
            throw new CustomException(MenuErrorCode.MENU_ACCESS_DENIED);
        }

        return CustomResponse.onSuccess(menuCommandService.changeQuantity(menuId, valueUpdate.changedValue()));
    }



    @PatchMapping("/menus/{menuId}/discountPercent")
    @Operation(summary = "메뉴 할인율 수정 API by 최현우", description = "role이 OWNER인 경우에 특정 메뉴의 할인율을 수정합니다.")
    public CustomResponse<MenuResDTO.UpdateMenu> changeDiscountPercent(
            @PathVariable Long menuId,
            @RequestBody @Valid MenuReqDTO.ValueUpdate valueUpdate,
            @CurrentUser AuthUser authUser
    ) {
        if (!authUser.getRole().equals(Role.OWNER)) {
            throw new CustomException(MenuErrorCode.MENU_ACCESS_DENIED);
        }

        return CustomResponse.onSuccess(menuCommandService.changeDiscountPercent(menuId, valueUpdate.changedValue()));
    }



    @DeleteMapping("/store/{storeId}/menu/{menuId}")
    @Operation(summary = "메뉴 삭제 API by 최현우", description = "role이 OWNER인 경우에 특정 가게의 특정 메뉴를 삭제합니다.")
    public CustomResponse<String> deleteMenu(
            @Parameter(description = "가게 ID", example = "1") @PathVariable Long storeId,
            @Parameter(description = "메뉴 ID", example = "8") @PathVariable Long menuId,
            @CurrentUser AuthUser authUser
    ) {

        if (!authUser.getRole().equals(Role.OWNER)) {
            throw new CustomException(MenuErrorCode.MENU_ACCESS_DENIED);
        }


        return CustomResponse.onSuccess("메뉴 삭제 성공");
    }
}

