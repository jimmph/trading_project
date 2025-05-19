package com.trading.service;

import java.util.List;

import com.trading.model.Coin;
import com.trading.model.User;
import com.trading.model.Watchlist;
import com.trading.repository.WatchlistRepository;

public class WatchlistServiceImpl implements WatchlistService {

    private WatchlistRepository watchlistRepository;

    public WatchlistServiceImpl(final WatchlistRepository watchlistRepository){
        this.watchlistRepository = watchlistRepository;
    }
    @Override
    public Watchlist findUserWatchlist(Long userId) throws Exception {
        Watchlist watchlist = watchlistRepository.findByUserId(userId);
        if(watchlist == null){
            throw new Exception("No watchlist found");
        }
        return watchlist;
    }

    @Override
    public Watchlist createWatchlist(User user) {
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);

        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist findById(Long id) throws Exception {
        return watchlistRepository.findById(id).orElseThrow(()->new Exception("no watchlist found"));
    }

    @Override
    public Coin addItemToWatchlist(Coin coin, User user) throws Exception{
        Watchlist watchlist = findUserWatchlist(user.getId());
        if (watchlist.getCoins().contains(coin)) watchlist.getCoins().remove(coin);
        else watchlist.getCoins().add(coin);
        watchlistRepository.save(watchlist);
        return coin;
    }

}
