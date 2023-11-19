package com.nordeus.jobfair.auctionservice.auctionservice.domain;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.*;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.AuctionNotifer;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.auction.AuctionStorage;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.user.UserStorage;
import com.nordeus.jobfair.auctionservice.auctionservice.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionServiceImpl implements AuctionService {
    private final AuctionNotifer auctionNotifer;
    private final AuctionStorage auctionStorage;
    private final UserStorage userStorage;

    /**
     * Get all active auctions. Also, could return auctions which inactivated just yet.
     * It could affect only view, so it isn't critical
     */
    @Override
    public Collection<Auction> getAllActive() {
        return auctionStorage.getAllActiveAuctions();
    }

    @Override
    public Auction getAuction(AuctionId auctionId) {
        return getAuctionOrThrowException(auctionId);
    }

    /**
     * Thread safe joining to auction. Including first bid
     */
    @Override
    public void join(AuctionId auctionId, User user) {
        Auction auction = getAuctionOrThrowException(auctionId);
        auction.applyNewBid(user.getUserId(), true);
    }

    /**
     * Thread safe bid
     */
    @Override
    public void bid(AuctionId auctionId, UserId userId) {
        Auction auction = getAuctionOrThrowException(auctionId);
        User user = getUserOrThrowException(userId);
        auction.applyNewBid(user.getUserId(), false)
                .ifPresent(auctionNotifer::bidPlaced);
    }

    /**
     * Get auction from storage or throw exception
     */
    private Auction getAuctionOrThrowException(AuctionId auctionId) {
        return auctionStorage.getAuctionByKey(auctionId)
                .orElseThrow(() -> {
                    log.error("AuctionId {} is not found in storage", auctionId);
                    return new DataNotFoundException(auctionId);
                });
    }

    /**
     * Get user from storage by id or throw exception
     */
    private User getUserOrThrowException(UserId userId) {
        return userStorage.findUser(userId)
                .orElseThrow(() -> {
                    log.error("UserId {} is not found in storage", userId);
                    return new DataNotFoundException(userId);
                });
    }
}
