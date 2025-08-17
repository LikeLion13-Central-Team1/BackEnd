package com.study.demo.backend.domain.favorite.service.FavoriteMenuService.command;

import org.springframework.stereotype.Service;

@Service
public interface FavoriteMenuCommandService {
    void create(Long userId, Long menuId);

    void delete(Long userId, Long menuId);
}
