package com.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.model.ForgotPasswordToken;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken, String>{

    ForgotPasswordToken findByUserId(Long userId);

}
