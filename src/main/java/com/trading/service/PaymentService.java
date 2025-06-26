package com.trading.service;

import com.trading.model.PaymentOrder;
import com.trading.model.User;
import com.trading.response.PaymentResponse;

public interface PaymentService {

    PaymentOrder createOrder(User user, Long amount);

    PaymentOrder getPaymentOrderById(Long id);

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId);

    PaymentResponse createSquarePaymentLink(User user, Long amount, Long orderId);
}
