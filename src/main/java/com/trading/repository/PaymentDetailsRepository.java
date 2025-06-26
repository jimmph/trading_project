package com.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.model.PaymentDetails;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long>{

    public PaymentDetails findByUserId(Long userId);
}
