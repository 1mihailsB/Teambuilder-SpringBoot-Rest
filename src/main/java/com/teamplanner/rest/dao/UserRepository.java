package com.teamplanner.rest.dao;

import com.teamplanner.rest.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>{

    User findByGooglesub(String username);

    Optional<User> findByNicknameEquals(String nickname);

    @Query("SELECT u FROM User u WHERE u.nickname IN (:friendNicknames) AND u.nickname NOT IN (SELECT gm.member.nickname FROM GameplanMember gm WHERE " +
            "gm.member.nickname IN (:friendNicknames) AND gm.gamePlan.id = :gameId)")
    List<User> findFriendsInvitableToGame(@Param("friendNicknames")List<String> userFriendNicknames, @Param("gameId") int gameid);
}
