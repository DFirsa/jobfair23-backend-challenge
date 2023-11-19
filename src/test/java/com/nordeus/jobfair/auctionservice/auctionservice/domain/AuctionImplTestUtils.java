package com.nordeus.jobfair.auctionservice.auctionservice.domain;

import java.util.ArrayList;
import java.util.List;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.storage.user.UserStorage;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuctionImplTestUtils {

    public static List<User> createUsersList(int count, UserStorage userStorage) {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = createSingleUser(userStorage);
            userList.add(user);
        }

        return userList;
    }

    public static User createSingleUser(UserStorage storage) {
        var user = new User();
        storage.save(user);
        return user;
    }
}
