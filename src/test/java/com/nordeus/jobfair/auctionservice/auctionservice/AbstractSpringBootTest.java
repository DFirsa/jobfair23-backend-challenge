package com.nordeus.jobfair.auctionservice.auctionservice;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootTest(properties = {
        "auction.initial-bid=0",
        "auction.ttl-ms=600000",
        "auction.refresh-offset-ms=50000"
})
@EnableScheduling
public class AbstractSpringBootTest {

}
