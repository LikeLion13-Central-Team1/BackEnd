package com.study.demo.backend.domain.favorite.service.FavoriteStoreService.query;

import com.study.demo.backend.domain.favorite.dto.response.FavoriteResDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Service;

@Service
public interface FavoriteStoreQueryService {
    FavoriteResDTO.FavoriteStoreList findMyFavorites(Long userId, Long cursor, @Min(1) @Max(50) int size);
}
