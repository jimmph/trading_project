package com.trading.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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
    
    @GetMapping
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        return ResponseEntity.ok(wallet);
    }

    @PutMapping("/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String jwt, 
            @PathVariable Long walletId, 
            @RequestBody WalletTransaction req) 
        throws Exception{
        User userSender = userService.findUserProfileByJwt(jwt);
        Wallet receiverWallet = walletService.getUserWallet(userSender);
        Wallet receiverWalletDone = walletService.walletToWalletTransfer(userSender, receiverWallet, req.getAmount());
        
        return ResponseEntity.ok(receiverWalletDone);
    }

    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            @RequestHeader("Authorization") String jwt, 
            @PathVariable Long orderId) 
        throws Exception{
        User userSender = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);
        Wallet walletReceiver = walletService.payOrderPayment(order, userSender);
        
        return ResponseEntity.ok(walletReceiver);
    }
}
