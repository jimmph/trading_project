package com.trading.service;


import org.springframework.stereotype.Service;

import com.trading.domain.VerificationType;
import com.trading.exception.VerificationCodeNotFoundException;
import com.trading.model.User;
import com.trading.model.VerificationCode;
import com.trading.repository.VerificationCodeRepository;
import com.trading.utils.OtpUtils;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService{

    private final VerificationCodeRepository verificationCodeRepository;

    public VerificationCodeServiceImpl(final VerificationCodeRepository verificationCodeRepository){
        this.verificationCodeRepository = verificationCodeRepository;
    }

    @Override
    public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
        VerificationCode verificationCode2 = new VerificationCode();
        verificationCode2.setOtp(OtpUtils.generateOtp());
        verificationCode2.setVerificationType(verificationType);
        verificationCode2.setUser(user);

        return verificationCodeRepository.save(verificationCode2);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) {
        return verificationCodeRepository.findById(id).orElseThrow(()-> new VerificationCodeNotFoundException("Verification code not found with id: "+id));

        
    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCodeById(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }

}
