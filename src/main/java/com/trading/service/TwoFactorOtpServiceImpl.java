package com.trading.service;

import org.springframework.stereotype.Service;

import com.trading.model.TwoFactorOTP;
import com.trading.model.User;
import com.trading.repository.TwoFactorOtpRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService{


    
    private TwoFactorOtpRepository twoFactorOtpRepository;

    public  TwoFactorOtpServiceImpl(final TwoFactorOtpRepository twoFactorOtpRepository){
        this.twoFactorOtpRepository = twoFactorOtpRepository;
    }

    @Override
    public TwoFactorOTP createTwoFactorOTP(User user, String otp, String jwt) {

        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        TwoFactorOTP twoFactorOTP = new TwoFactorOTP();
        twoFactorOTP.setId(id);
        twoFactorOTP.setJwt(jwt);
        twoFactorOTP.setOtp(otp);
        twoFactorOTP.setUser(user);
        
        return twoFactorOtpRepository.save(twoFactorOTP);

    }

    @Override
    public TwoFactorOTP findByUser(Long userId) {
        return twoFactorOtpRepository.findByUserId(userId);
    }
    @Override
    public TwoFactorOTP findById(String id) {

        Optional<TwoFactorOTP> opt = twoFactorOtpRepository.findById(id);
        return opt.orElse(null);
    }

    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp) {
        return twoFactorOTP.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP) {
        twoFactorOtpRepository.delete(twoFactorOTP);
    }
}
