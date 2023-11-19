package com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.user;

import java.util.List;
import java.util.Optional;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.UserId;

public interface UserStorage {
    void save(User user);
    Optional<User> findUser(UserId userId);
    List<User> findAll();
    void remove(User user);
}
