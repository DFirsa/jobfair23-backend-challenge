package com.nordeus.jobfair.auctionservice.auctionservice.exception;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.AuctionId;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.UserId;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(AuctionId auctionId) {
        super("Auction with id : %s is not found".formatted(auctionId));
    }

    public DataNotFoundException(UserId userId) {
        super("User with id: %s is not found".formatted(userId));
    }
}
