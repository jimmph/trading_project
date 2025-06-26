package com.trading.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trading.model.PaymentDetails;
import com.trading.model.User;
import com.trading.service.PaymentDetailsService;
import com.trading.service.UserService;

@RestController
@RequestMapping("/api")
public class PaymentDetailsController {

    private final PaymentDetailsService paymentDetailsService;
    private final UserService userService;

    
    public PaymentDetailsController(final PaymentDetailsService paymentDetailsService, final UserService userService){
        this.paymentDetailsService = paymentDetailsService;
        this.userService = userService;
    }

    @PostMapping("/payment-details")
    public ResponseEntity<PaymentDetails> addPaymentDetails(@RequestHeader("Authorization") String jwt, @RequestBody PaymentDetails paymentDetailsRequest) throws Exception{

        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailsService.addPaymentDetails(paymentDetailsRequest.getAccountHolderName(), paymentDetailsRequest.getIban(), paymentDetailsRequest.getBic(), paymentDetailsRequest.getBankName(), user);

        return new ResponseEntity<>(paymentDetails, HttpStatus.CREATED);
    }

    @GetMapping("/payment-details") 
    public ResponseEntity<PaymentDetails> getUserPaymentDetails(@RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailsService.getUserPaymentDetails(user);

        return new ResponseEntity<> (paymentDetails, HttpStatus.CREATED);
    }
}
