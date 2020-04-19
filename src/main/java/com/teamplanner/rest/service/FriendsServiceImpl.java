package com.teamplanner.rest.service;

import com.teamplanner.rest.dao.FriendsRepository;
import com.teamplanner.rest.model.entity.Friendship;
import com.teamplanner.rest.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendsServiceImpl implements FriendsService {

    private FriendsRepository friendsRepository;

    @Autowired
    public FriendsServiceImpl(FriendsRepository friendsRepository) {
        this.friendsRepository = friendsRepository;
    }

    @Override
    public List<Friendship> findAll() {
        return friendsRepository.findAll();
    }

    @Override
    public List<Friendship> findAllForUser(User user) {
        return friendsRepository.findByInvitingUserOrInvitedUser(user, user);
    }

    @Override
    public List<Friendship> findIncomingFriendRequests(User user) {
        return friendsRepository.findByInvitedUserAndStatusEquals(user, 0);
    }

    @Override
    public List<Friendship> findOutgoingFriendRequests(User user) {
        return friendsRepository.findByInvitingUserAndStatusEquals(user, 0);
    }

    @Override
    public List<Friendship> findAllConfirmedFriendRequests(User user) {
        return friendsRepository.findByInvitingUserAndStatusEqualsOrInvitedUserAndStatusEquals(user,1, user, 1);
    }

    @Override
    public Friendship checkForFriendshipOrPendingRquests(User invitingUser, User invitedUser) {
        return friendsRepository.findByInvitingUserAndInvitedUserOrInvitingUserAndInvitedUser(invitingUser, invitedUser,
                invitedUser, invitingUser);
    }

    @Override
    public Friendship findById(int id) {
        Optional<Friendship> result = friendsRepository.findById(id);

        Friendship friendship;

        if(result.isPresent()){
            friendship = result.get();
        }else {
            friendship = null;
        }
        return friendship;
    }

    @Override
    public void save(Friendship friendship) {
        friendsRepository.save(friendship);
    }

    @Override
    public void deleteById(int id) {
        friendsRepository.deleteById(id);
    }
}
