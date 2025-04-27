package com.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.model.OrderItem;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{


}
