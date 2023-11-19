package com.nordeus.jobfair.auctionservice.auctionservice.domain.executor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.AuctionTerminationStatusEnum;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.AuctionNotifer;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.auction.AuctionStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StopAuctionExecutor {

    private final AuctionStorage auctionStorage;
    private final AuctionNotifer auctionNotifer;

    @Scheduled(fixedRate = 5000)
    public void stopAuction() {
        log.info("Start job for refreshing/stopping auctions");
        var auctions = auctionStorage.getAllActiveAuctions();
        var auctionsGroup = auctions.stream().collect(Collectors.groupingBy(Auction::markAsInactive));

        var refreshedAuctions = getAuctionsByStatus(AuctionTerminationStatusEnum.REFRESHED, auctionsGroup);
        log.info("Found {} refreshed auctions", refreshedAuctions.size());
        if (!refreshedAuctions.isEmpty()) {
            auctionNotifer.activeAuctionsRefreshed(refreshedAuctions);
        }

        var endedAuctions = getAuctionsByStatus(AuctionTerminationStatusEnum.EXPIRED, auctionsGroup);
        log.info("Found {} ended auctions", endedAuctions.size());
        if (!endedAuctions.isEmpty()) {
            endedAuctions.forEach(auctionNotifer::auctionFinished);
        }
        log.info("Updating auction status job finished");
    }

    private List<Auction> getAuctionsByStatus(
            AuctionTerminationStatusEnum status,
            Map<AuctionTerminationStatusEnum, List<Auction>> map
    ) {
        return map.getOrDefault(status, List.of());
    }
}
