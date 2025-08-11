package com.study.demo.backend.domain.user.controller;

import com.study.demo.backend.domain.user.dto.request.UserLocationReqDTO;
import com.study.demo.backend.domain.user.dto.response.UserLocationResDTO;
import com.study.demo.backend.domain.user.service.command.UserLocationCommandService;
import com.study.demo.backend.domain.user.service.query.UserLocationQueryService;
import com.study.demo.backend.global.apiPayload.CustomResponse;
import com.study.demo.backend.global.security.annotation.CurrentUser;
import com.study.demo.backend.global.security.userdetails.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/locations")
@Tag(name = "사용자 위치 API", description = "사용자 위치 관련 API")
public class UserLocationController {

    private final UserLocationCommandService userLocationCommandService;
    private final UserLocationQueryService userLocationQueryService;

    @PostMapping("")
    @Operation(summary = "사용자 위치 등록 API by 김지명", description = "로그인 한 사용자의 위치를 등록합니다.")
    public CustomResponse<UserLocationResDTO.LocationInfo> createLocation(@RequestBody UserLocationReqDTO.Create reqDTO,
                                                                          @CurrentUser AuthUser authUser) {
        UserLocationResDTO.LocationInfo resDTO = userLocationCommandService.createLocation(reqDTO, authUser);
        return CustomResponse.onSuccess(resDTO, "위치 등록 성공");
    }

    @GetMapping("")
    @Operation(summary = "사용자 위치 목록 조회 API by 김지명", description = "로그인 한 사용자의 등록된 위치 목록을 조회합니다.")
    public CustomResponse<UserLocationResDTO.LocationInfoList> getLocationList(@CurrentUser AuthUser authUser) {
        UserLocationResDTO.LocationInfoList resDTO = userLocationQueryService.getLocationList(authUser);
        return CustomResponse.onSuccess(resDTO, "위치 목록 조회 성공");
    }

    @GetMapping("/active")
    @Operation(summary = "활성화 된 사용자 위치 조회 API by 김지명", description = "로그인 한 사용자의 활성화 된 위치를 조회합니다.")
    public CustomResponse<UserLocationResDTO.LocationInfo> getActiveLocation(@CurrentUser AuthUser authUser) {
        UserLocationResDTO.LocationInfo resDTO = userLocationQueryService.getActiveLocation(authUser);
        return CustomResponse.onSuccess(resDTO, "활성화 된 위치 조회 성공");
    }

    @PatchMapping("/active/{id}")
    @Operation(summary = "사용자 활성화 위치 변경 API by 김지명", description = "로그인 한 사용자의 활성화 위치를 변경합니다.")
    public CustomResponse<Void> changeActiveLocation(@PathVariable("id") Long id, @CurrentUser AuthUser authUser) {
        userLocationCommandService.changeActiveLocation(id, authUser);
        return CustomResponse.onSuccess((Void) null, "활성화 위치 변경 완료");
    }

    @PatchMapping("/{id}")
    @Operation(summary = "사용자 위치 정보 변경 API by 김지명", description = "로그인 한 사용자의 위치 정보를 변경합니다.")
    public CustomResponse<UserLocationResDTO.LocationInfo> modifyLocationInfo(@PathVariable("id") Long id,
                                                                              @CurrentUser AuthUser authUser,
                                                                              @RequestBody UserLocationReqDTO.Modify reqDTO) {
        UserLocationResDTO.LocationInfo resDTO = userLocationCommandService.modifyLocationInfo(id, authUser, reqDTO);
        return CustomResponse.onSuccess(resDTO, "위치 정보 변경 완료");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "사용자 위치 삭제 API by 김지명", description = "로그인 한 사용자의 위치 정보를 변경합니다.")
    public CustomResponse<Void> deleteLocation(@PathVariable("id") Long id, @CurrentUser AuthUser authUser) {
        userLocationCommandService.deleteLocation(id, authUser);
        return CustomResponse.onSuccess((Void) null, "위치 삭제 완료");
    }
}
