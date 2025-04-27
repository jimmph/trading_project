package com.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.model.Watchlist;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    Watchlist findByUserId(Long userId );
}
