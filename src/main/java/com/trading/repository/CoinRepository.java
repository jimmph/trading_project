package com.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.model.Coin;

public interface CoinRepository extends JpaRepository<Coin, String> {

}
