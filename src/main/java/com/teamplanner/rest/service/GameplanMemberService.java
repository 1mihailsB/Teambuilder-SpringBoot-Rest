package com.teamplanner.rest.service;

import com.teamplanner.rest.model.entity.GamePlan;
import com.teamplanner.rest.model.entity.GameplanMember;
import com.teamplanner.rest.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GameplanMemberService {
    public GameplanMember findExistingGameplanMember(User member, GamePlan gamePlan);

    public GameplanMember save(GameplanMember gameplanMember);

    public GameplanMember findById(int id);

    public List<GameplanMember> findIncomingGameInvites(User member);

    public List<GameplanMember> findAcceptedGameInvites(User member);

    public List<GameplanMember> findAllMembersByGameplan(GamePlan gameplan);

    public List<GameplanMember> deleteAllByGameplan(GamePlan gamePlan);

    public GameplanMember declineGameInvite(int id);

    public void deleteById(int id);

    public void deleteRemovedFriendFromGames(User member, List<GamePlan> gamePlans);
}
