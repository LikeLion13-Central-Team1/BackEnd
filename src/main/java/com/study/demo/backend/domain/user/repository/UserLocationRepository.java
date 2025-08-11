package com.study.demo.backend.domain.user.repository;

import com.study.demo.backend.domain.user.entity.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {
}
