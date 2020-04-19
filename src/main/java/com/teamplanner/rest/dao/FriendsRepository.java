package com.teamplanner.rest.dao;

import com.teamplanner.rest.model.entity.Friendship;
import com.teamplanner.rest.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendsRepository extends JpaRepository<Friendship, Integer> {

    /** all friend requests related to user, regardless of the status (pending/confirmed) */
    List<Friendship> findByInvitingUserOrInvitedUser(User user, User sameUser);

    /** incoming friend requests related to user, matching the status (pending/confirmed) */
    List<Friendship> findByInvitedUserAndStatusEquals(User user, int status);

    /** outgoing friend requests related to user, matching the status (pending/confirmed) */
    List<Friendship> findByInvitingUserAndStatusEquals(User user, int status);

    /** all friend requests related to user, matching the status (pending/confirmed) */
    List<Friendship> findByInvitingUserAndStatusEqualsOrInvitedUserAndStatusEquals(User user, int status, User sameUser, int sameStatus);

    /** check if friendship/pending request already exists between two given users */
    Friendship findByInvitingUserAndInvitedUserOrInvitingUserAndInvitedUser(User invitingUser, User invitedUser, User sameInvitedUser, User sameInvitingUser);

}
