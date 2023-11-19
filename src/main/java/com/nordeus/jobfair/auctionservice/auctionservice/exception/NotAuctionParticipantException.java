package com.nordeus.jobfair.auctionservice.auctionservice.exception;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.AuctionId;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.UserId;

public class NotAuctionParticipantException extends RuntimeException {
    public NotAuctionParticipantException(AuctionId auctionId, UserId userId) {
        super("User %s doesn't participate in auction %s".formatted(userId, auctionId));
    }
}
