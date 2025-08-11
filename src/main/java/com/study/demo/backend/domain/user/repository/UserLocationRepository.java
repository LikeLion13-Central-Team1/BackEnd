package com.study.demo.backend.domain.user.repository;

import com.study.demo.backend.domain.user.entity.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {

    @Query("SELECT ul FROM UserLocation ul WHERE ul.user.id = :userId ORDER BY ul.createdAt DESC")
    List<UserLocation> findAllLocationsByUserId(@Param("userId") Long userId);

    @Query("SELECT ul FROM UserLocation ul WHERE ul.user.id = :userId AND ul.active = true")
    Optional<UserLocation> findActiveLocationByUserId(@Param("userId") Long userId);
}
