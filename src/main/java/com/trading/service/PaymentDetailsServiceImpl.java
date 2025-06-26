package com.trading.service;

import org.springframework.stereotype.Service;

import com.trading.model.PaymentDetails;
import com.trading.model.User;
import com.trading.repository.PaymentDetailsRepository;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService{

    private PaymentDetailsRepository paymentDetailsRepository;

    public PaymentDetailsServiceImpl(final PaymentDetailsRepository paymentDetailsRepository)
    {
        this.paymentDetailsRepository=paymentDetailsRepository;
    }
    
    @Override
    public PaymentDetails addPaymentDetails(String accountHolderName, String iban, String bic, String bankName, User user) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAccountHolderName(accountHolderName);
        paymentDetails.setBankName(bankName);
        paymentDetails.setBic(bic);
        paymentDetails.setIban(iban);
        paymentDetails.setUser(user);
        return paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUserPaymentDetails(User user) {
        return paymentDetailsRepository.findByUserId(user.getId());
    }

}
