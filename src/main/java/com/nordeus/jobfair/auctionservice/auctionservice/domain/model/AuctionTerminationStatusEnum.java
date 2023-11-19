package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

public enum AuctionTerminationStatusEnum {
    /**
     * Auction marked as finished
     */
    EXPIRED,
    /**
     * Auction is refreshed
     */
    REFRESHED,
    /**
     * Auction isn't expired yet
     */
    IN_PROGRESS
}
