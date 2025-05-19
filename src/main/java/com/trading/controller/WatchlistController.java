package com.trading.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trading.model.Coin;
import com.trading.model.User;
import com.trading.model.Watchlist;
import com.trading.service.CoinService;
import com.trading.service.UserService;
import com.trading.service.WatchlistService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final UserService userService;
    private final CoinService coinService;

    public WatchlistController( final WatchlistService watchlistService, final UserService userService, final CoinService coinService){
        this.coinService =coinService;
        this.userService = userService;
        this.watchlistService = watchlistService;
    }

    @GetMapping("/user")
    public ResponseEntity<Watchlist> getUserWatchlist(@RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        Watchlist watchlist= watchlistService.findUserWatchlist(user.getId());
        return ResponseEntity.ok(watchlist); 
    }

    @PostMapping("/create")
    public ResponseEntity<Watchlist> createWatchlist(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Watchlist watchlist = watchlistService.createWatchlist(user);
        return ResponseEntity.ok(watchlist);
    }
    
    @GetMapping("/{watchlistId}")
    public ResponseEntity<Watchlist> getWatchlistById(@PathVariable Long watchlistId) throws Exception{
        Watchlist watchlist = watchlistService.findById(watchlistId);
        return ResponseEntity.ok(watchlist);
    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchlist(@RequestHeader String jwt, @PathVariable String coinId) throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.findById(coinId);
        Coin addedCoin= watchlistService.addItemToWatchlist(coin, user);
        return ResponseEntity.ok(addedCoin);


    }

    
}
