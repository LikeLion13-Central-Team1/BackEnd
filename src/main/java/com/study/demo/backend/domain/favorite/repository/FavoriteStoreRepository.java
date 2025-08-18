package com.study.demo.backend.domain.favorite.repository;

import com.study.demo.backend.domain.favorite.entity.favoriteStore.FavoriteStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteStoreRepository extends JpaRepository<FavoriteStore, Long> {

    boolean existsByUserIdAndStoreId(Long userId, Long storeId);

    Optional<FavoriteStore> findByUserIdAndStoreId(Long userId, Long userId1);

    @Query(value = """
            SELECT fs.*
            FROM favorite_store fs
            WHERE fs.user_id = :userId
              AND (:cursorId IS NULL OR fs.id < :cursorId)
            ORDER BY fs.id DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<FavoriteStore> findByUserIdOrderByIdDesc(
            @Param("userId") Long userId,
            @Param("cursorId") Long cursorId,
            @Param("limit") int limit
    );

    @Query("""
            select fs.store.id
            from FavoriteStore fs
            where fs.user.id = :userId and fs.store.id in :storeIds
            """)
    List<Long> findFavoriteStoreIds(@Param("userId") Long userId,
                                    @Param("storeIds") List<Long> storeIds
    );
}
