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
    SELECT r FROM Review r
    JOIN r.order o
    JOIN OrderMenu om ON om.order = o
    WHERE om.menu.id = :menuId
      AND r.reviewDate < :cursor
    ORDER BY r.reviewDate DESC
    """)
    List<Review> findReviewsByMenuIdBefore(
            @Param("menuId") Long menuId,
            @Param("cursor") LocalDateTime cursor,
            Pageable pageable
    );
}
