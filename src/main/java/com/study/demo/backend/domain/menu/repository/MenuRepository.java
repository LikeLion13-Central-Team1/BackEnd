package com.study.demo.backend.domain.menu.repository;

import com.study.demo.backend.domain.menu.entity.Menu;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByIdAndStoreId(Long id, Long storeId);

    boolean existsByNameAndStoreIdAndIdNot(String name, Long storeId, Long id);

    boolean existsByNameAndStoreId(String name, Long storeId);

    // 가격 오름차순 : 가격이 같으면 id가 작은 것 부터
    @Query(value = """

            SELECT m.*
    FROM menu m
    WHERE (:storeId IS NULL OR m.store_id = :storeId)
      AND (
            :cursor IS NULL
            OR (m.price, m.id) > (
                (SELECT m2.price FROM menu m2 WHERE m2.id = :cursor),
                :cursor
            )
          )
    ORDER BY m.price ASC, m.id ASC
    LIMIT :limit
    """, nativeQuery = true)
    List<Menu> findMenusByPriceAsc(@Param("storeId") Long storeId, @Param("cursor") Long cursor, @Param("limit") int limit);

    // 가격 내림차순 : 가격이 같으면 id가 큰 것 부터
    @Query(value = """
    SELECT m.*
    FROM menu m
    WHERE (:storeId IS NULL OR m.store_id = :storeId)
      AND (
            :cursor IS NULL
            OR (m.price, m.id) < (
                (SELECT m2.price FROM menu m2 WHERE m2.id = :cursor),
                :cursor
            )
          )
    ORDER BY m.price DESC, m.id DESC
    LIMIT :limit
    """, nativeQuery = true)
    List<Menu> findMenusByPriceDesc(@Param("storeId") Long storeId, @Param("cursor") Long cursor, @Param("limit") int limit);

    // 할인율 내림차순 : 할인율이 같으면 id 오름차순으로
    @Query(value = """
    SELECT m.*
    FROM menu m
    WHERE (:storeId IS NULL OR m.store_id = :storeId)
      AND (
            :cursor IS NULL
            OR (COALESCE(m.discount_percent, 0), m.id) < (
                (SELECT COALESCE(m2.discount_percent, 0) FROM menu m2 WHERE m2.id = :cursor),
                :cursor
            )
          )
    ORDER BY COALESCE(m.discount_percent, 0) DESC, m.id ASC
    LIMIT :limit
    """, nativeQuery = true)
    List<Menu> findMenusByDiscountDesc(@Param("storeId") Long storeId, @Param("cursor") Long cursor, @Param("limit") int limit);

}
