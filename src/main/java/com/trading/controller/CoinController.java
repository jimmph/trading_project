package com.trading.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.model.Coin;
import com.trading.service.CoinService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/coins")
public class CoinController {

    private final CoinService coinService;
    private final ObjectMapper objectMapper;

    public CoinController(final CoinService coinService, final ObjectMapper objectMapper){
        this.coinService = coinService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    ResponseEntity<List<Coin>> getCoinList(@RequestParam("page") int page) throws Exception{
        List<Coin> coins = coinService.getCoinList(page);
        return new ResponseEntity<>(coins, HttpStatus.ACCEPTED);
    }


 
    @GetMapping("/{coinId}/chart")
    ResponseEntity<JsonNode> getMarketChart(
        @PathVariable String coinId, 
        @RequestParam("days") int days
    ) throws Exception{
        String marketChart = coinService.getMarketChart(coinId, days);
        JsonNode jsonNode =  objectMapper.readTree(marketChart);
        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    ResponseEntity<JsonNode> searchCoin (
        @RequestParam("q") String keyword
    ) throws Exception{
        String coin = coinService.searchCoin(keyword);
        JsonNode jsonNode =  objectMapper.readTree(coin);
        return ResponseEntity.ok(jsonNode);
    }

    @GetMapping("/top50")
    ResponseEntity<JsonNode> getTop50CoinByMarketCapRank () throws Exception{
        String coin = coinService.getTop50CoinsByMarketCapRank();
        JsonNode jsonNode =  objectMapper.readTree(coin);
        return ResponseEntity.ok(jsonNode);
    }

    @GetMapping("/trading")
    ResponseEntity<JsonNode> getTradingCoin() throws Exception{
        String coin = coinService.getTradingCoins();
        JsonNode jsonNode =  objectMapper.readTree(coin);
        return ResponseEntity.ok(jsonNode);
    }

    @GetMapping("/details/{coinId}")
    ResponseEntity<JsonNode> getCoinDetails (
        @PathVariable String coinId) throws Exception{
        String coin = coinService.getCoinDetails(coinId);
        JsonNode jsonNode =  objectMapper.readTree(coin);
        return ResponseEntity.ok(jsonNode);
    }
}
    
