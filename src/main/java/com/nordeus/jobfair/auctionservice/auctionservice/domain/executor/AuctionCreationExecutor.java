package com.nordeus.jobfair.auctionservice.auctionservice.domain.executor;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.AuctionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionCreationExecutor {

    private final AuctionFactory auctionFactory;

    @Scheduled(fixedRate = 600000)
    public void createNewAuctions() {
        for (int i = 0; i < 10; i++) {
            log.info("Creating new auctions job started...");
            auctionFactory.createAuction();
            log.info("Creating new auctions job successfully finished");
        }
    }
}
