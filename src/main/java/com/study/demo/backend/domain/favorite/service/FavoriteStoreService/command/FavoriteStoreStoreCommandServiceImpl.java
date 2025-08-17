package com.study.demo.backend.domain.favorite.service.FavoriteStoreService.command;

import com.study.demo.backend.domain.favorite.entity.favoriteStore.FavoriteStore;
import com.study.demo.backend.domain.favorite.exception.FavoriteErrorCode;
import com.study.demo.backend.domain.favorite.repository.FavoriteStoreRepository;
import com.study.demo.backend.domain.store.entity.Store;
import com.study.demo.backend.domain.store.exception.StoreErrorCode;
import com.study.demo.backend.domain.store.repository.StoreRepository;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.domain.user.exception.UserErrorCode;
import com.study.demo.backend.domain.user.repository.UserRepository;
import com.study.demo.backend.global.apiPayload.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteStoreStoreCommandServiceImpl implements FavoriteStoreCommandService {

    private final FavoriteStoreRepository favoriteStoreRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Override
    public void create(Long userId, Long storeId) {
        if (favoriteStoreRepository.existsByUserIdAndStoreId(userId, storeId)){
            throw new CustomException(FavoriteErrorCode.ALREADY_FAVORITED);
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(StoreErrorCode.STORE_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        try {
            favoriteStoreRepository.save(FavoriteStore.of(user, store));
        } catch (DataIntegrityViolationException ignore) {}
    }

    @Override
    public void delete(Long userId, Long storeId) {
        favoriteStoreRepository.findByUserIdAndStoreId(userId, storeId)
                .ifPresent(favoriteStoreRepository::delete);
    }
}
