package com.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.model.TwoFactorOTP;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOTP, String> {

    TwoFactorOTP findByUserId(Long userId);
}
