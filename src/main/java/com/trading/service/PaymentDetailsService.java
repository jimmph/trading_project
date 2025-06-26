package com.trading.service;

import com.trading.model.PaymentDetails;
import com.trading.model.User;

public interface PaymentDetailsService {

    public PaymentDetails addPaymentDetails(String accountHolderName, 
    String iban, 
    String bic, 
    String bankName, 
    User user);

    public PaymentDetails getUserPaymentDetails(User user);

}
