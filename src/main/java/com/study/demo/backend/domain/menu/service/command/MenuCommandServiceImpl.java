package com.study.demo.backend.domain.menu.service.command;

import com.study.demo.backend.domain.menu.converter.MenuConverter;
import com.study.demo.backend.domain.menu.dto.request.MenuReqDTO;
import com.study.demo.backend.domain.menu.dto.response.MenuResDTO;
import com.study.demo.backend.domain.menu.entity.Menu;
import com.study.demo.backend.domain.menu.exception.MenuErrorCode;
import com.study.demo.backend.domain.menu.exception.MenuException;
import com.study.demo.backend.domain.menu.repository.MenuRepository;
import com.study.demo.backend.domain.store.entity.Store;
import com.study.demo.backend.domain.store.exception.StoreErrorCode;
import com.study.demo.backend.domain.store.exception.StoreException;
import com.study.demo.backend.domain.store.repository.StoreRepository;
import com.study.demo.backend.global.ImgUploader.FileUploader;
import com.study.demo.backend.global.apiPayload.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MenuCommandServiceImpl implements MenuCommandService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final FileUploader fileUploader;

    @Override
    public MenuResDTO.CreateMenu createMenu(Long storeId, MenuReqDTO.CreateMenu create, MultipartFile menuImage) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(StoreErrorCode.STORE_NOT_FOUND));

        if (menuRepository.existsByNameAndStoreId(create.name(), storeId)) {
            throw new CustomException(MenuErrorCode.MENU_ALREADY_EXISTS);
        }

        if (create.price() == null || create.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException(MenuErrorCode.INVALID_PRICE);
        }

        Integer percent = (create.discountPercent() == null) ? 0 : create.discountPercent();
        if (percent < 0 || percent > 100) {
            throw new CustomException(MenuErrorCode.INVALID_DISCOUNT_PERCENT);
        }

        if (create.quantity() == null || create.quantity() < 0) {
            throw new CustomException(MenuErrorCode.INVALID_QUANTITY);
        }

        String imageUrl = (menuImage != null && !menuImage.isEmpty())
                ? fileUploader.upload(menuImage, "menu")
                : null;

        Menu menu = MenuConverter.toEntity(create, imageUrl, store);

        menu.calcDiscountPrice();

        menuRepository.save(menu);

        return MenuConverter.toMenuCreateRes(menu);
    }

    @Override
    public MenuResDTO.UpdateMenu updateMenu(
            Long storeId, Long menuId,
            MenuReqDTO.UpdateMenu update,
            MultipartFile menuImage
    ) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(StoreErrorCode.STORE_NOT_FOUND));

        Menu menu = menuRepository.findByIdAndStoreId(menuId, storeId)
                .orElseThrow(() -> new CustomException(MenuErrorCode.MENU_NOT_FOUND));

        if (update.name() != null && !update.name().isBlank()
                && menuRepository.existsByNameAndStoreIdAndIdNot(update.name(), storeId, menuId)) {
            throw new CustomException(MenuErrorCode.MENU_ALREADY_EXISTS);
        }

        if (update.price() != null && update.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException(MenuErrorCode.INVALID_PRICE);
        }
        if (update.discountPercent() != null) {
            int p = update.discountPercent();
            if (p < 0 || p > 100) throw new CustomException(MenuErrorCode.INVALID_DISCOUNT_PERCENT);
        }
        if (update.quantity() != null && update.quantity() < 0) {
            throw new CustomException(MenuErrorCode.INVALID_QUANTITY);
        }

        String imageUrl = (menuImage != null && !menuImage.isEmpty())
                ? fileUploader.upload(menuImage, "menu")
                : null;

        menu.updateNameDescription(update.name(), update.description());
        menu.updatePriceDiscount(
                update.price() != null ? update.price() : menu.getPrice(),
                update.discountPercent() != null ? update.discountPercent() : menu.getDiscountPercent()
        );
        menu.updateQuantity(update.quantity());
        menu.updateImage(imageUrl);

        return MenuConverter.toMenuUpdateRes(menu);
    }


    @Override
    public MenuResDTO.UpdateMenu changeQuantity(Long menuId, int value) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(MenuErrorCode.MENU_NOT_FOUND));

        int newQuantity = Math.max(0, menu.getQuantity() + value);
        menu.updateQuantity(newQuantity);

        return MenuConverter.toMenuUpdateRes(menu);
    }

    @Override
    public MenuResDTO.UpdateMenu changeDiscountPercent(Long menuId, int value) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(MenuErrorCode.MENU_NOT_FOUND));

        int newPercent = Math.max(0, Math.min(100, menu.getDiscountPercent() + value));
        menu.updatePriceDiscount(menu.getPrice(),newPercent);

        return MenuConverter.toMenuUpdateRes(menu);
    }


    @Override
    public void deleteMenu(Long storeId, Long menuId) {
        if (!storeRepository.existsById(storeId)) {
            throw new StoreException(StoreErrorCode.STORE_NOT_FOUND);
        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(MenuErrorCode.MENU_NOT_FOUND));

        if (!menu.getStore().getId().equals(storeId)) {
            throw new MenuException(MenuErrorCode.MENU_STORE_MISMATCH);
        }

        menuRepository.delete(menu);
    }
}
