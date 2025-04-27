package com.trading.service;

import java.util.List;

import com.trading.model.Coin;

public interface CoinService {

    List<Coin> getCoinList(int page) throws Exception;
    
    String getMarketChart(String coinId, int days) throws Exception;

    String getCoinDetails(String coinId) throws Exception;

    Coin findById(String coinId) throws Exception;

    String searchCoin(String keyWord) throws Exception;

    String getTop50CoinsByMarketCapRank() throws Exception;

    String getTradingCoins() throws Exception;
}
