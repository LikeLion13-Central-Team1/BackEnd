package com.study.demo.backend.domain.review.repository;

import com.study.demo.backend.domain.order.entity.Order;
import com.study.demo.backend.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByOrder(Order order);

    @Query("""
        SELECT DISTINCT r FROM Review r
        JOIN FETCH r.user
        JOIN FETCH r.order o
        JOIN FETCH o.orderMenus om
        JOIN FETCH om.menu m
        WHERE m.id = :menuId
        AND (:cursorDate IS NULL OR r.reviewDate < :cursorDate)
        ORDER BY r.reviewDate DESC
        """)
    List<Review> findReviewsByMenuId(
            @Param("menuId") Long menuId,
            @Param("cursorDate") LocalDateTime cursorDate,
            Pageable pageable
    );

    @Query("""
        SELECT DISTINCT r FROM Review r
        JOIN FETCH r.user
        JOIN FETCH r.order o
        JOIN FETCH o.orderMenus om
        JOIN FETCH om.menu m
        JOIN FETCH m.store s
        WHERE s.id = :storeId
        AND (:cursorDate IS NULL OR r.reviewDate < :cursorDate)
        ORDER BY r.reviewDate DESC
        """)
    List<Review> findReviewsByStoreId(
            @Param("storeId") Long storeId,
            @Param("cursorDate") LocalDateTime cursorDate,
            Pageable pageable
    );

    @Query("select r from Review r where r.store.id = :storeId order by r.reviewDate desc")
    List<Review> findAllByStoreIdOrderByReviewDateAtDesc(@Param("storeId") Long storeId);

    @Query("""
    SELECT DISTINCT r FROM Review r
    JOIN FETCH r.user u
    JOIN FETCH r.order o
    JOIN FETCH o.orderMenus om
    JOIN FETCH om.menu m
    JOIN FETCH m.store s
    WHERE u.id = :userId
    AND (:cursor IS NULL OR r.id < :cursor)
    ORDER BY r.id DESC
    """)
    List<Review> findReviewsByUserId(
            @Param("userId") Long userId,
            @Param("cursor") Long cursor,
            Pageable pageable
    );
}
