package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.StorableModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class User implements StorableModel<UserId> {
    private final UserId userId;

    public User() {
        userId = new UserId();
    }

    @Override
    public UserId getKey() {
        return userId;
    }
}
