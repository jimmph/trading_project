package com.trading.service;

import org.springframework.stereotype.Service;

import com.squareup.square.SquareClient;
import com.squareup.square.core.Environment;
import com.trading.domain.PaymentOrderStatus;
import com.trading.model.PaymentOrder;
import com.trading.model.User;
import com.trading.repository.PaymentRepository;
import com.trading.response.PaymentResponse;
import org.springframework.beans.factory.annotation.Value;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final String squareSecretKey;


    public PaymentServiceImpl(final PaymentRepository paymentRepository, @Value("${square.api.key}") String squareSecretKey){
        this.paymentRepository = paymentRepository;
        this.squareSecretKey = squareSecretKey;
    }

    @Override
    public PaymentOrder createOrder(User user, Long amount) {
        PaymentOrder paymentOrder = new PaymentOrder() ;
        paymentOrder.setUser(user);
        paymentOrder.setAmount(amount);

        return paymentRepository.save(paymentOrder);

    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) {
        return paymentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Payment order not found"));
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) {
        if (!paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
            return false;
        }

        try {
            SquareClient squareClient = SquareClient.builder()
                .token(squareSecretKey)  // Ou bearerToken selon version SDK
                .environment(Environment.SANDBOX) // Ou PRODUCTION
                .build();

            // Appel à l'API Square pour récupérer le paiement
            var paymentResponse = squareClient.payments().get(paymentId);

            // Gestion des erreurs API Square
            if (paymentResponse.getErrors() != null && !paymentResponse.getErrors().isEmpty()) {
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentRepository.save(paymentOrder);
                return false;
            }

            var payment = paymentResponse.getPayment();

            if (payment != null) {
                // Récupérer le statut du paiement (exemple : COMPLETED, FAILED, PENDING, CANCELED)
                String status = payment.getStatus();

                // Récupérer le montant payé (en cents ou la plus petite unité monétaire)
                Long amount = payment.getAmountMoney().getAmount();

                if ("COMPLETED".equalsIgnoreCase(status)) {
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                } else {
                    paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                }

                paymentRepository.save(paymentOrder);
                return paymentOrder.getStatus().equals(PaymentOrderStatus.SUCCESS);
            } else {
                // Aucun paiement trouvé
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentRepository.save(paymentOrder);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            paymentOrder.setStatus(PaymentOrderStatus.FAILED);
            paymentRepository.save(paymentOrder);
            return false;
        }
    }

    @Override
    public PaymentResponse createSquarePaymentLink(User user, Long amount, Long orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSquarePaymentLink'");
    }

    