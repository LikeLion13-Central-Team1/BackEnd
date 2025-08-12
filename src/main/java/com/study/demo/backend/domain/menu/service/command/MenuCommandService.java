package com.study.demo.backend.domain.menu.service.command;

import com.study.demo.backend.domain.menu.dto.request.MenuReqDTO;
import com.study.demo.backend.domain.menu.dto.response.MenuResDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface MenuCommandService {
    MenuResDTO.CreateMenu createMenu(Long storeId, MenuReqDTO.CreateMenu create, MultipartFile menuImage);

    MenuResDTO.UpdateMenu updateMenu(Long storeId, Long menuId,MenuReqDTO.UpdateMenu update,MultipartFile menuImage);

    MenuResDTO.UpdateMenu changeQuantity(Long menuId, int delta);

    MenuResDTO.UpdateMenu changeDiscountPercent(Long menuId, int delta);

    void deleteMenu(Long storeId, Long menuId);
}