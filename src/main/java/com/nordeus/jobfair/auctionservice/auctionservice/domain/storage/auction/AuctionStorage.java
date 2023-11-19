package com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.auction;

import java.util.Collection;
import java.util.Optional;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.AuctionId;

/**
 * Auction Storage Interface
 */
public interface AuctionStorage {
    Optional<Auction> getAuctionByKey(AuctionId auctionId);
    Collection<Auction> getAllAuctions();
    Collection<Auction> getAllActiveAuctions();
    void save(Auction auction);
    void remove(Auction auction);
}
