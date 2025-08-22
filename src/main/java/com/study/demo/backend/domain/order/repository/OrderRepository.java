package com.study.demo.backend.domain.order.repository;

import com.study.demo.backend.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 주문 생성 시간 내림차순( 최신순 )
    @Query(value = """
            SELECT * FROM orders o
            WHERE (:cursor IS NULL OR o.id < :cursor)
            ORDER BY o.id DESC
            LIMIT :size
            """, nativeQuery = true)
    List<Order> findAllOrders(@Param("cursor") Long cursor, @Param("size") int size);

    @Query(value = """
                SELECT * FROM orders o
                WHERE o.user_id = :userId
                  AND (:cursor IS NULL OR o.id < :cursor)
                ORDER BY o.id DESC
                LIMIT :limit
            """, nativeQuery = true)
    List<Order> findByUserIdWithCursor(@Param("userId") Long userId, @Param("cursor") Long cursor, @Param("limit") int limit);

    @Query(value = """
                SELECT * FROM orders o
                WHERE o.store_id = :storeId
                  AND (:cursor IS NULL OR o.id < :cursor)
                ORDER BY o.id DESC
                LIMIT :limit
            """, nativeQuery = true)
    List<Order> findByStoreIdWithCursor(@Param("storeId") Long storeId, @Param("cursor") Long cursor, @Param("limit") int limit);
}
