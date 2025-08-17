package com.study.demo.backend.domain.favorite.service.FavoriteMenuService.query;

import com.study.demo.backend.domain.favorite.dto.response.FavoriteResDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Service;

@Service
public interface FavoriteMenuQueryService {
    FavoriteResDTO.FavoriteMenuList findMyFavorites(Long userId, Long cursor, @Min(1) @Max(50) int size);
}
