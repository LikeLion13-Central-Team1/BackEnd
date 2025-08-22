package com.study.demo.backend.domain.store.repository;

import com.study.demo.backend.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    boolean existsByName(String name);

    // 최신순 정렬
    @Query(value = """
        SELECT * FROM store
        WHERE (:cursor IS NULL OR id < :cursor)
        ORDER BY created_at DESC
        LIMIT :size
    """, nativeQuery = true)
    List<Store> findStoresByCreatedAt(
            @Param("cursor") Long cursor,
            @Param("size") int size
    );

    // 거리순 정렬(정확한 거리 계산)
    @Query(value = """
    SELECT * FROM store
    WHERE (:cursor IS NULL OR id < :cursor)
    ORDER BY ST_Distance_Sphere(
        POINT(:lng, :lat),
        POINT(longitude, latitude)
    ) ASC
    LIMIT :size
    """, nativeQuery = true)
    List<Store> findStoresByDistance(
            @Param("cursor") Long cursor,
            @Param("size") int size,
            @Param("lat") double lat,
            @Param("lng") double lng
    );


    // 리뷰 개수 순 정렬
    @Query(value = """
    SELECT  s.*
    FROM    store s
    LEFT JOIN (
        SELECT  r.store_id, COUNT(*) AS cnt
        FROM    review r
        GROUP BY r.store_id
    ) rc ON rc.store_id = s.id
    WHERE   (:cursor IS NULL OR s.id < :cursor)
    ORDER BY COALESCE(rc.cnt, 0) DESC, s.created_at DESC
    LIMIT   :size
""", nativeQuery = true)
    List<Store> findStoresByReview(
            @Param("cursor") Long cursor,
            @Param("size") int size
    );

    @Query(value = "SELECT id FROM store WHERE user_id = :userId LIMIT 1", nativeQuery = true)
    Optional<Long> findIdByUserId(@Param("userId") Long userId);

    boolean existsByUser_Id(Long userId);
}
