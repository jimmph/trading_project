package com.trading.service;

import java.util.List;

import com.trading.domain.OrderType;
import com.trading.model.Coin;
import com.trading.model.Order;
import com.trading.model.OrderItem;
import com.trading.model.User;

public interface OrderService {
    Order createOrder(User user, OrderItem orderItem, OrderType orderType);

    Order getOrderById(Long orderId) throws Exception;

    List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbolString);

     Order buyAsset(Coin coin, double quantity, User user) throws Exception;

     Order sellAsset(Coin coin, double quantity, User user) throws Exception;

    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;


}
