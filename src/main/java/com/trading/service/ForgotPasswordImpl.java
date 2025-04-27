package com.trading.service;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.trading.domain.VerificationType;
import com.trading.model.ForgotPasswordToken;
import com.trading.model.User;
import com.trading.repository.ForgotPasswordRepository;

@Service
public class ForgotPasswordImpl implements ForgotPasswordService{

    private ForgotPasswordRepository forgotPasswordRepository;
    
    public ForgotPasswordImpl(final ForgotPasswordRepository forgotPasswordRepository){
        this.forgotPasswordRepository=forgotPasswordRepository;
    }

    @Override
    public ForgotPasswordToken createToken(User user, String id,  String otp, VerificationType verificationType, String sendTo) {
        ForgotPasswordToken token = new ForgotPasswordToken();
        token.setUser(user);
        token.setSendTo(sendTo);
        token.setVerificationType(verificationType);
        token.setOtp(otp);
        token.setId(id);
        return forgotPasswordRepository.save(token);  

    }

    @Override
    public ForgotPasswordToken findbyId(String id) {
        Optional<ForgotPasswordToken> token = forgotPasswordRepository.findById(id);
        return token.orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUser(Long userId) {
        return forgotPasswordRepository.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgotPasswordToken token) {
        forgotPasswordRepository.delete(token);
    }

    
}
