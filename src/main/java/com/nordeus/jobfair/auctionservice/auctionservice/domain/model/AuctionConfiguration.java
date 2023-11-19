package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties("auction")
public class AuctionConfiguration {
    private long initialBid;
    private long ttlMs;
    private long refreshOffsetMs;
}
