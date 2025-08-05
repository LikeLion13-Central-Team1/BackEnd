package com.study.demo.backend.domain.order.repository;

import com.study.demo.backend.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
