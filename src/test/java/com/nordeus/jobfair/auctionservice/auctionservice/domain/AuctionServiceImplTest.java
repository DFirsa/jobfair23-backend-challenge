package com.nordeus.jobfair.auctionservice.auctionservice.domain;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.nordeus.jobfair.auctionservice.auctionservice.AbstractSpringBootTest;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.AuctionId;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.UserId;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.AuctionFactory;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.auction.InMemoryAuctionStorage;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.user.InMemoryUserStorage;
import com.nordeus.jobfair.auctionservice.auctionservice.exception.DataNotFoundException;
import com.nordeus.jobfair.auctionservice.auctionservice.exception.NotAuctionParticipantException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.nordeus.jobfair.auctionservice.auctionservice.domain.AuctionImplTestUtils.createSingleUser;
import static com.nordeus.jobfair.auctionservice.auctionservice.domain.AuctionImplTestUtils.createUsersList;
import static org.junit.jupiter.api.Assertions.*;

class AuctionServiceImplTest extends AbstractSpringBootTest {

    @Autowired
    AuctionServiceImpl auctionService;
    @Autowired
    InMemoryUserStorage userStorage;
    @Autowired
    InMemoryAuctionStorage auctionStorage;
    @Autowired
    AuctionFactory auctionFactory;

    @BeforeEach
    void cleanUpInMemoryStorage() {
        userStorage.findAll().forEach(userStorage::remove);
        auctionStorage.getAllAuctions().forEach(auctionStorage::remove);
    }

    @Test
    @DisplayName("Test joining logic")
    void testJoin() {
        var user = createSingleUser(userStorage);
        var auction = auctionFactory.createAuction();

        auctionService.join(auction.getAuctionId(), user);

        assertEquals(1, auction.getAuctionParticipants().size());
        assertTrue(auction.getAuctionParticipants().contains(user.getUserId()));

        assertEquals(user.getUserId(), auction.getLeadingBid().getBidOwner());
        assertEquals(1, auction.getLeadingBid().getAmount());
    }

    @Test
    @DisplayName("Test that every concurrent bid is applied")
    void bidConcurencyTest() throws InterruptedException {
        var executorService = Executors.newFixedThreadPool(100);
        List<User> users = createUsersList(1000, userStorage);
        Auction auction = auctionFactory.createAuction();

        users.forEach(user -> {
            auctionService.join(auction.getAuctionId(), user);
            for (int i = 0; i < 9; i++) {
                executorService.submit(() -> {
                   auctionService.bid(auction.getAuctionId(), user.getUserId());
                });
            }
        });

        executorService.awaitTermination(10, TimeUnit.SECONDS);
        // 1k joining bids + 9k bids
        assertEquals(10000, auction.getLeadingBid().getAmount());
    }

    @Test
    @DisplayName("Auction isn't found by id")
    void failedBidTestCauseOfAuctionIdIsNotFound() {
        var user = createSingleUser(userStorage);
        var auction = auctionFactory.createAuction();
        auctionService.join(auction.getAuctionId(), user);

        assertThrows(DataNotFoundException.class, () -> auctionService.bid(new AuctionId(), user.getUserId()));
    }

    @Test
    @DisplayName("User isn't found by id")
    void failedBidTestCauseOfUserIdIsNotFound() {
        var user = createSingleUser(userStorage);
        var auction = auctionFactory.createAuction();
        auctionService.join(auction.getAuctionId(), user);

        assertThrows(DataNotFoundException.class, () -> auctionService.bid(auction.getAuctionId(), new UserId()));
    }

    @Test
    @DisplayName("User does not participate")
    void failedBidTestCauseOfJoining() {
        var user = createSingleUser(userStorage);
        var auction = auctionFactory.createAuction();

        assertThrows(NotAuctionParticipantException.class, () -> auctionService.bid(auction.getAuctionId(), user.getUserId()));
    }
}