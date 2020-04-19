package com.teamplanner.rest.service;

import com.teamplanner.rest.model.entity.Friendship;
import com.teamplanner.rest.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FriendsService {

    public List<Friendship> findAll();

    public List<Friendship> findAllForUser(User user);

    public List<Friendship> findIncomingFriendRequests(User user);

    public List<Friendship> findOutgoingFriendRequests(User user);

    public List<Friendship> findAllConfirmedFriendRequests(User user);

    public Friendship checkForFriendshipOrPendingRquests(User invitingUser, User invitedUser);

    public Friendship findById(int id);

    public void save(Friendship friendship);

    public void deleteById(int id);
}
