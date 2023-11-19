package com.nordeus.jobfair.auctionservice.auctionservice.domain.executor;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.auction.AuctionStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Executor which cleans all inactive auctions, because of useless
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionStorageCleanerExecutor {

    private final AuctionStorage auctionStorage;

    @Scheduled(fixedDelay = 60000)
    public void cleanInactiveAuctionsFromMemory() {
        log.info("Starting cleaning memory from auctions");

        var auctions =  auctionStorage.getAllAuctions();
        log.info("Found auctions before cleaning: {}", auctions.size());

        var auctionsToClean = auctions.stream()
                .filter(auction -> !auction.isActive())
                .toList();
        log.info("Found {} inactove auctions to clean", auctionsToClean.size());

        auctionsToClean.forEach(auctionStorage::remove);
    }

}
