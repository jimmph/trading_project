package com.trading.controller;

import java.net.http.HttpResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trading.model.Order;
import com.trading.model.User;
import com.trading.model.Wallet;
import com.trading.model.WalletTransaction;
import com.trading.service.OrderService;
import com.trading.service.UserService;
import com.trading.service.WalletService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    private final WalletService walletService;
    private final UserService userService;
    private final OrderService orderService;
    public WalletController(final WalletService walletService, final UserService userService, final OrderService orderservice){
        this.walletService = walletService;
        this.userService= userService;
        this.orderService = orderservice;
    }
    
    @GetMapping("/api/wallet")
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("api/wallet/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String jwt, 
            @PathVariable Long walletId, 
            @RequestBody WalletTransaction req) 
        throws Exception{
        User userSender = userService.findUserProfileByJwt(jwt);
        Wallet receiverWallet = walletService.getUserWallet(userSender);
        Wallet wallet = walletService.walletToWalletTransfer(userSender, receiverWallet, req.getAmount());
        
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            @RequestHeader("Authorization") String jwt, 
            @PathVariable Long orderId) 
        throws Exception{
        User userSender = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);
        Wallet wallet = walletService.payOrderPayment(order, userSender);
        
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }
}
