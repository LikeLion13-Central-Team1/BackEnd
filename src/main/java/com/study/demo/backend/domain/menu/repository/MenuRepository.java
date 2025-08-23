package com.study.demo.backend.domain.menu.repository;

import com.study.demo.backend.domain.menu.entity.Menu;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByIdAndStoreId(Long id, Long storeId);

    boolean existsByNameAndStoreIdAndIdNot(String name, Long storeId, Long id);

    boolean existsByNameAndStoreId(String name, Long storeId);

    // 가격 오름차순: 가격이 같으면 id가 작은 것 부터
    @Query("SELECT m " +
            "FROM Menu m " +
            "JOIN FETCH m.store s " +
            "WHERE (:storeId IS NULL OR s.id = :storeId) AND ((:cursorId IS NULL) " +
            "OR (m.price > (SELECT m2.price FROM Menu m2 WHERE m2.id = :cursorId)) " +
            "OR (m.price = (SELECT m3.price FROM Menu m3 WHERE m3.id = :cursorId) AND m.id > :cursorId)) " +
            "ORDER BY m.price ASC, m.id ASC")
    List<Menu> findMenusByPriceAsc(@Param("storeId") Long storeId, @Param("cursorId") Long cursorId, Pageable pageable);

    // 가격 내림차순: 가격이 같으면 id가 큰 것 부터
    @Query("SELECT m " +
            "FROM Menu m " +
            "JOIN FETCH m.store s " +
            "WHERE (:storeId IS NULL OR s.id = :storeId) AND ((:cursorId IS NULL) " +
            "OR (m.price < (SELECT m2.price FROM Menu m2 WHERE m2.id = :cursorId)) " +
            "OR (m.price = (SELECT m3.price FROM Menu m3 WHERE m3.id = :cursorId) AND m.id < :cursorId)) " +
            "ORDER BY m.price DESC, m.id DESC")
    List<Menu> findMenusByPriceDesc(@Param("storeId") Long storeId, @Param("cursorId") Long cursorId, Pageable pageable);

    // 할인율 내림차순: 할인율이 같으면 id 오름차순으로
    @Query("SELECT m " +
            "FROM Menu m " +
            "JOIN FETCH m.store s " +
            "WHERE (:storeId IS NULL OR s.id = :storeId) AND ((:cursorId IS NULL) " +
            "OR (COALESCE(m.discountPercent, 0) < (SELECT COALESCE(m2.discountPercent, 0) FROM Menu m2 WHERE m2.id = :cursorId)) " +
            "OR (COALESCE(m.discountPercent, 0) = (SELECT COALESCE(m3.discountPercent, 0) FROM Menu m3 WHERE m3.id = :cursorId) AND m.id > :cursorId)) " +
            "ORDER BY m.discountPercent DESC, m.id ASC")
    List<Menu> findMenusByDiscountDesc(@Param("storeId") Long storeId, @Param("cursorId") Long cursorId, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Menu m
           SET m.quantity = 0
         WHERE m.store.id = :storeId
           AND m.quantity <> 0
    """)
    void zeroQuantitiesByStoreId(@Param("storeId") Long storeId);
}
