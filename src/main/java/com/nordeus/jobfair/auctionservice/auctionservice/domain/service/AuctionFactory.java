package com.nordeus.jobfair.auctionservice.auctionservice.domain.service;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.AuctionConfiguration;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.auction.AuctionStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionFactory {
    private final AuctionConfiguration auctionConfiguration;
    private final AuctionStorage auctionStorage;

    public Auction createAuction() {
        Auction auction = new Auction(auctionConfiguration);
        auctionStorage.save(auction);
        log.info("Auction {} successfully created", auction);
        return auction;
    }
}
