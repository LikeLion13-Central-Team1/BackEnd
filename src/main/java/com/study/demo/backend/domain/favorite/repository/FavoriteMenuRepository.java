package com.study.demo.backend.domain.favorite.repository;

import com.study.demo.backend.domain.favorite.entity.favoriteMenu.FavoriteMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteMenuRepository extends JpaRepository<FavoriteMenu, Long> {
    boolean existsByUserIdAndMenuId(Long userId, Long menuId);

    Optional<FavoriteMenu> findByUserIdAndMenuId(Long userId, Long menuId);

    @Query(value = """
            SELECT fm.*
            FROM favorite_menu fm
            WHERE fm.user_id = :userId
              AND (:cursorId IS NULL OR fm.id < :cursorId)
            ORDER BY fm.id DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<FavoriteMenu> findByUserIdOrderByIdDesc(
            @Param("userId") Long userId,
            @Param("cursorId") Long cursorId,
            @Param("limit") int limit
    );

    @Query("""
                select fm.menu.id
                from FavoriteMenu fm
                where fm.user.id = :userId
                  and fm.menu.id in :menuIds
            """)
    List<Long> findFavoriteMenuIds(
            @Param("userId") Long userId,
            @Param("menuIds") List<Long> menuIds
    );
}
