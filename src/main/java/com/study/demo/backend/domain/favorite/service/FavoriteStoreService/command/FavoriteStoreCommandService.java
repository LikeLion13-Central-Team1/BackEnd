package com.study.demo.backend.domain.favorite.service.FavoriteStoreService.command;

import org.springframework.stereotype.Service;

@Service
public interface FavoriteStoreCommandService {
    void create(Long userId, Long menuId);

    void delete(Long userId, Long menuId);
}
