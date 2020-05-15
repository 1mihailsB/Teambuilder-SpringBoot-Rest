package com.teamplanner.rest.service;

import com.teamplanner.rest.dao.GameplanMembersRepository;
import com.teamplanner.rest.model.entity.GamePlan;
import com.teamplanner.rest.model.entity.GameplanMember;
import com.teamplanner.rest.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameplanMemberServiceImpl implements GameplanMemberService {
    GameplanMembersRepository gameplanMembersRepository;

    @Autowired
    public GameplanMemberServiceImpl(GameplanMembersRepository gameplanMembersRepository) {
        this.gameplanMembersRepository = gameplanMembersRepository;
    }

    @Override
    public GameplanMember findExistingGameplanMember(User member, GamePlan gamePlan) {
        return gameplanMembersRepository.findByGamePlanAndMember(gamePlan, member);
    }

    @Override
    public GameplanMember save(GameplanMember gameplanMember) {
        return gameplanMembersRepository.saveAndFlush(gameplanMember);
    }

    @Override
    public GameplanMember findById(int id) {
        Optional<GameplanMember> result = gameplanMembersRepository.findById(id);

        GameplanMember gameplanMember;

        if(result.isPresent()){
            gameplanMember = result.get();
        }else{
            gameplanMember = null;
        }

        return gameplanMember;
    }

    @Override
    public List<GameplanMember> findIncomingGameInvites(User member) {
        return gameplanMembersRepository.findByMemberAndStatusEquals(member, 0);
    }

    @Override
    public List<GameplanMember> findAcceptedGameInvites(User member) {
        return gameplanMembersRepository.findByMemberAndStatusEquals(member, 1);
    }

    @Override
    public List<GameplanMember> findAllMembersByGameplan(GamePlan gameplan) {
        return gameplanMembersRepository.findByGamePlanAndStatusEquals(gameplan, 1);
    }

    @Override
    public List<GameplanMember> deleteAllByGameplan(GamePlan gamePlan) {
        return gameplanMembersRepository.deleteByGamePlan(gamePlan);
    }

    @Override
    public GameplanMember declineGameInvite(int id) {
        return gameplanMembersRepository.deleteById(id);
    }

    @Override
    public void deleteById(int id) {
        gameplanMembersRepository.deleteById(id);
    }

    @Override
    public void deleteRemovedFriendFromGames(User member, List<GamePlan> gamePlans) {
        gameplanMembersRepository.deleteByMemberAndGamePlanIn(member,gamePlans);
    }
}

