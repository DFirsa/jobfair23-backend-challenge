package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

import java.time.Instant;

import lombok.Data;

@Data
public class Bid {
    /**
     * Total bid's value
     */
    private final long amount;
    /**
     * User who owns this bid
     */
    private final UserId bidOwner;
    /**
     * Time when bid is applied
     */
    private final Instant timestamp;

    public Bid(UserId userId, long amount) {
        this.amount = amount;
        this.bidOwner = userId;
        timestamp = Instant.now();
    }

    /**
     * Beat this bid
     */
    public Bid nextBid(UserId userId) {
        return new Bid(userId, amount + 1);
    }
}
