package com.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.model.PaymentOrder;

public interface PaymentRepository extends JpaRepository<PaymentOrder, Long>{

    public PaymentOrder findByUserId(Long userId);
}
