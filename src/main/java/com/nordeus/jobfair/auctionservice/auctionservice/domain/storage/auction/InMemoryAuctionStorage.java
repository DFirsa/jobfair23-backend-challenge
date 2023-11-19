package com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.auction;

import java.util.Collection;
import java.util.Optional;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.AuctionId;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.InMemoryStorage;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.auction.AuctionStorage;
import org.springframework.stereotype.Service;

@Service
public class InMemoryAuctionStorage extends InMemoryStorage<AuctionId, Auction> implements AuctionStorage {

    @Override
    public Optional<Auction> getAuctionByKey(AuctionId auctionId) {
        return getByKey(auctionId);
    }

    @Override
    public Collection<Auction> getAllAuctions() {
        return getAll();
    }

    @Override
    public Collection<Auction> getAllActiveAuctions() {
        return getAll().stream()
                .filter(Auction::isActive)
                .toList();
    }

    @Override
    public void save(Auction auction) {
        addValue(auction);
    }

    @Override
    public void remove(Auction auction) {
        delete(auction.getKey());
    }


}
