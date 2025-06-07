package com.shopshop.firstshop.repository;

import com.shopshop.firstshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
} 