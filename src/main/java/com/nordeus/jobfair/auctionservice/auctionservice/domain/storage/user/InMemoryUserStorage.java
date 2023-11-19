package com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.user;

import java.util.List;
import java.util.Optional;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.UserId;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.InMemoryStorage;
import org.springframework.stereotype.Service;

@Service
public class InMemoryUserStorage extends InMemoryStorage<UserId, User> implements UserStorage {
    @Override
    public void save(User user) {
        addValue(user);
    }

    @Override
    public Optional<User> findUser(UserId userId) {
        return getByKey(userId);
    }

    @Override
    public List<User> findAll() {
        return getAll().stream().toList();
    }

    @Override
    public void remove(User user) {
        delete(user.getUserId());
    }
}
