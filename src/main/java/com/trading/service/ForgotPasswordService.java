package com.trading.service;

import com.trading.domain.VerificationType;
import com.trading.model.ForgotPasswordToken;
import com.trading.model.User;

public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user, String id, String otp, VerificationType verificationType, String sendTo);

    ForgotPasswordToken findbyId(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);

}
