package com.trading.controller;

import org.springframework.web.bind.annotation.RestController;

import com.trading.domain.OrderType;
import com.trading.model.Coin;
import com.trading.model.Order;
import com.trading.model.User;
import com.trading.request.CreateOrderRequest;
import com.trading.service.CoinService;
import com.trading.service.OrderService;
import com.trading.service.UserService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class OrderController {

    private OrderService orderService;
    private UserService userService;
    private CoinService coinService;

    public OrderController(final OrderService orderService, final UserService userService, final CoinService coinService)
    {
        this.orderService = orderService;
        this.coinService = coinService;
        this.userService = userService;
    }

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(@RequestHeader("Authorization") String jwt, @RequestBody CreateOrderRequest req) throws Exception {
        User user= userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.findById(req.getCoinId());
        Order order = orderService.processOrder(coin, req.getQuantity(), req.getOrderType(), user);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwt, @RequestParam Long orderId) throws Exception {
        
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);

        if(order.getUser().getId().equals(user.getId())){
            return ResponseEntity.ok(order);
        }
        else{
            throw new Exception("You don't have access");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersForUser(@RequestHeader("Authorization") String jwt, @RequestParam(required = false) OrderType orderType, @RequestParam(required = false) String asset_symbol) throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orders = orderService.getAllOrdersOfUser(user.getId(), orderType, asset_symbol);
        return ResponseEntity.ok(orders);
    }
    
    
}
