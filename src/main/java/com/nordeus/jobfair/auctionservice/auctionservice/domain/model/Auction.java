package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.StorableModel;
import com.nordeus.jobfair.auctionservice.auctionservice.exception.NotAuctionParticipantException;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
public class Auction implements StorableModel<AuctionId> {

    /**
     * Mock user for initial bid
     */
    private static final UserId NOBODY = new UserId();
    /**
     * Id for auction
     */
    private final AuctionId auctionId;
    /**
     * List of user who joined to this auction
     */
    private final Set<UserId> auctionParticipants;
    /**
     * Time when auction started
     */
    private final Instant auctionStartTimestamp;
    /**
     * Configuration for auction
     */
    private final AuctionConfiguration auctionConfiguration;
    /**
     * Bid that is current leader
     */
    private Bid leadingBid;
    /**
     * Is active
     */
    private boolean isActive;

    public Auction(AuctionConfiguration auctionConfiguration) {
        this.auctionId = new AuctionId();
        this.auctionParticipants = new HashSet<>();
        this.leadingBid = new Bid(NOBODY, auctionConfiguration.getInitialBid());
        this.isActive = true;
        this.auctionStartTimestamp = Instant.now();
        this.auctionConfiguration = auctionConfiguration;
    }

    /**
     * Apply bid for new user if auction is active. Thread-safe implementation
     * true if bid is successfully applied
     */
    public synchronized Optional<Bid> applyNewBid(@NonNull UserId userId, boolean isJoining) {
        log.info("Applying new bid for auction: {}", auctionId);
        if (isActive) {
            if (!auctionParticipants.contains(userId) && !isJoining) {
                log.error("User {} doesn't participate in auction {}", userId, auctionId);
                throw new NotAuctionParticipantException(auctionId, userId);
            }

            leadingBid = leadingBid.nextBid(userId);
            log.info("Bid for user {} successfully applied", userId);

            if (isJoining) {
                auctionParticipants.add(userId);
            }
            return Optional.of(leadingBid);
        }

        log.info("Auction {} is inactive. Bid for user {} isn't applied", auctionId, userId);
        return Optional.empty();
    }

    /**
     * returns status based on marking result. Thread-safe implementation
     * If auction is refreshed - REFRESHED
     * If auction is ended - EXPIRED
     * If auction isn't ends yet - IN_PROGRESS
     */
    public synchronized AuctionTerminationStatusEnum markAsInactive() {
        Instant now = Instant.now();

        if (!isExpired(now)) {
            log.info("Auction started at {}, now: {}. Auction in progress", auctionStartTimestamp, now);
            return AuctionTerminationStatusEnum.IN_PROGRESS;
        }

        long msSinceLastBid = ChronoUnit.MILLIS.between(leadingBid.getTimestamp(), now);
        // check if should refresh ttl timer
        if (auctionConfiguration.getRefreshOffsetMs() >= msSinceLastBid) {
            log.info(
                    "Auction started at {}, now: {}, refresh time: {}. Auction in refreshed",
                    auctionStartTimestamp,
                    now,
                    leadingBid.getTimestamp()
            );
            return AuctionTerminationStatusEnum.REFRESHED;
        }

        isActive = false;
        log.info(
                "Auction started at {}, now: {}. Auction is expired and marked as inactive",
                auctionStartTimestamp,
                now
        );
        return AuctionTerminationStatusEnum.EXPIRED;
    }

    private boolean isExpired(Instant now) {
        long msFromStart = ChronoUnit.MILLIS.between(auctionStartTimestamp, now);
        return msFromStart >= auctionConfiguration.getTtlMs();
    }

    @Override
    public AuctionId getKey() {
        return auctionId;
    }
}
