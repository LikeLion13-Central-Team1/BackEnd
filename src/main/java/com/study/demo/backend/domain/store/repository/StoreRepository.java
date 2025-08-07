package com.study.demo.backend.domain.store.repository;

import com.study.demo.backend.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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


    // 리뷰순 정렬
    @Query(value = """
        SELECT * FROM store
        WHERE (:cursor IS NULL OR id < :cursor)
        ORDER BY review_count DESC, created_at DESC
        LIMIT :size
    """, nativeQuery = true)
    List<Store> findStoresByReview(
            @Param("cursor") Long cursor,
            @Param("size") int size
    );
}
